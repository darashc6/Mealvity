package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.adapters.AutocompleteStreetRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.autocompleteaddress.StreetList
import cenec.mealvity.mealvity.classes.config.SharedPreferencesConfig
import cenec.mealvity.mealvity.classes.constants.ApiAccess
import cenec.mealvity.mealvity.classes.dialogs.InsertPasswordDialog
import cenec.mealvity.mealvity.classes.dialogs.InsertStreetNumberDialog
import cenec.mealvity.mealvity.classes.interfaceapi.HereApi
import cenec.mealvity.mealvity.classes.retrofit.CustomRetrofitBuilder
import cenec.mealvity.mealvity.classes.singleton.StreetSingleton
import cenec.mealvity.mealvity.classes.util.StreetUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AutocompleteStreetActivity : AppCompatActivity(), InsertStreetNumberDialog.InsertStreetNumberListener {
    private lateinit var api: HereApi
    private lateinit var streetSelected: String
    private lateinit var rvAdapter: AutocompleteStreetRecyclerViewAdapter
    private var streetList = StreetList(arrayListOf())
    private val hereRetrofitBuilder by lazy { CustomRetrofitBuilder.createRetrofitBuilder(ApiAccess.URL_HERE_API) }
    private val customSearchBar by lazy { findViewById<View>(R.id.custom_search_bar) }
    private val streetRecyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view_street_list) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autocomplete_street)

        initApi()
        setupEditTextChangeListener()
        setupRecyclerView()
        setupBackPressSearchBar()
    }

    private fun initApi() {
        api = hereRetrofitBuilder.create(HereApi::class.java)
    }

    private fun setupEditTextChangeListener() {
        val editText = customSearchBar.findViewById<EditText>(R.id.street_edit_text)

        editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val queryToSearch = s.toString()
                if (queryToSearch.length > 1) {
                    val call = api.getAutocompleteAddresses(queryToSearch)

                    call.enqueue(object : Callback<StreetList> {
                        override fun onFailure(call: Call<StreetList>, t: Throwable) {
                            Toast.makeText(this@AutocompleteStreetActivity, "onFailure", Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<StreetList>, response: Response<StreetList>) {
                            println(response.raw().request().url())
                            if (response.isSuccessful) {
                                val list = response.body()
                                list?.let {
                                    streetList = it
                                    rvAdapter.setStreetList(streetList)
                                    rvAdapter.notifyDataSetChanged()
                                }
                            } else {
                                Toast.makeText(this@AutocompleteStreetActivity, response.code().toString(), Toast.LENGTH_LONG).show()
                            }
                        }

                    })
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

        })
    }

    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        streetRecyclerView.layoutManager = rvLayoutManager

        rvAdapter = AutocompleteStreetRecyclerViewAdapter(StreetList(arrayListOf()))
        rvAdapter.setAutocompleteStreetRecyclerViewListener(object : AutocompleteStreetRecyclerViewAdapter.AutocompleteStreetRecyclerViewListener {
            override fun onClick(position: Int) {
                streetSelected = streetList.results[position].streetName
                val streetNumberDialog = InsertStreetNumberDialog(this@AutocompleteStreetActivity)
                streetNumberDialog.show(supportFragmentManager, "")
            }

        })
        streetRecyclerView.adapter = rvAdapter
    }

    private fun setupBackPressSearchBar() {
        val backSearchBar = customSearchBar.findViewById<ImageView>(R.id.exit_activity)

        backSearchBar.setOnClickListener{
            finish()
        }
    }

    override fun getStreetNumber(number: Int) {
        StreetSingleton.setStreet(StreetUtil.reformatedStreet(streetSelected, number))
        finish()
    }
}
