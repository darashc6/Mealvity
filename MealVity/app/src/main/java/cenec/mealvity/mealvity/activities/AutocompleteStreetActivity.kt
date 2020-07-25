package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityAutocompleteStreetBinding
import cenec.mealvity.mealvity.classes.adapters.AutocompleteStreetRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.autocompleteaddress.StreetList
import cenec.mealvity.mealvity.classes.config.SharedPreferencesConfig
import cenec.mealvity.mealvity.classes.constants.ApiAccess
import cenec.mealvity.mealvity.classes.interfaceapi.HereApi
import cenec.mealvity.mealvity.classes.retrofit.CustomRetrofitBuilder
import cenec.mealvity.mealvity.classes.singleton.StreetSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activity where the user types an address and comes up with some autocomplete suggestions
 */
class AutocompleteStreetActivity : AppCompatActivity() {
    private val hereRetrofitBuilder by lazy { CustomRetrofitBuilder.createRetrofitBuilder(ApiAccess.URL_HERE_API) } // Retrofit builder for the API
    private val api by lazy { hereRetrofitBuilder.create(HereApi::class.java) }
    private lateinit var rvAdapter: AutocompleteStreetRecyclerViewAdapter // Adapter of the RecyclerView
    private var streetList = StreetList(arrayListOf()) // List of streets
    private lateinit var binding: ActivityAutocompleteStreetBinding // View binding of the activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutocompleteStreetBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupEditTextChangeListener()
        setupRecyclerView()
        setupBackPressSearchBar()
    }

    /**
     * Sets up the text change listener in the EditText
     * Every time there is a modification in the EditText, it sends a request to the API, retrieving all the suggestions from the EditText
     */
    private fun setupEditTextChangeListener() {
        val editText = binding.customSearchBar.streetEditText

        editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val queryToSearch = s.toString()
                if (queryToSearch.length > 1) {
                    val call = api.getAutocompleteAddresses(queryToSearch)

                    call.enqueue(object : Callback<StreetList> {
                        override fun onFailure(call: Call<StreetList>, t: Throwable) {
                            Toast.makeText(this@AutocompleteStreetActivity, resources.getString(R.string.api_call_onFailure), Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<StreetList>, response: Response<StreetList>) {
                            if (response.isSuccessful) {
                                val list = response.body()
                                list?.let {
                                    streetList = it
                                    rvAdapter.setStreetList(streetList)
                                    rvAdapter.notifyDataSetChanged()
                                }
                            } else {
                                Toast.makeText(this@AutocompleteStreetActivity, "${resources.getString(R.string.api_call_response_unsuccessful)} ${response.code()}", Toast.LENGTH_LONG).show()
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

    /**
     * Sets up the RecyclerView containing the list of street names
     */
    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewStreetList.layoutManager = rvLayoutManager

        rvAdapter = AutocompleteStreetRecyclerViewAdapter(StreetList(arrayListOf()))
        rvAdapter.setAutocompleteStreetRecyclerViewListener(object : AutocompleteStreetRecyclerViewAdapter.AutocompleteStreetRecyclerViewListener {
            override fun onClick(streetName: String) {
                // Every time a user selects a street, it will be saved in the preferences
                val prefs = SharedPreferencesConfig(this@AutocompleteStreetActivity)
                prefs.saveDefaultStreet(streetName)
                StreetSingleton.setStreet(streetName)
                finish()
            }

        })
        binding.recyclerViewStreetList.adapter = rvAdapter
    }

    /**
     * Sets up the back press in the search bar
     * When the back button is pressed, it closes the activity
     */
    private fun setupBackPressSearchBar() {
        val backSearchBar = binding.customSearchBar.exitActivity

        backSearchBar.setOnClickListener{
            finish()
        }
    }
}
