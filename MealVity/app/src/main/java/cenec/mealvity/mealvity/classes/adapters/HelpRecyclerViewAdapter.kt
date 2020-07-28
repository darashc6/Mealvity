package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.databinding.ItemListHelpQuestionBinding
import cenec.mealvity.mealvity.classes.help.Help

class HelpRecyclerViewAdapter(private val listQuestions: ArrayList<Help>): RecyclerView.Adapter<HelpRecyclerViewAdapter.HelpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpViewHolder {
        val _binding = ItemListHelpQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HelpViewHolder(_binding)
    }

    override fun getItemCount(): Int {
        return listQuestions.size
    }

    override fun onBindViewHolder(holder: HelpViewHolder, position: Int) {
        holder.bind(listQuestions[position])
    }

    class HelpViewHolder(_binding: ItemListHelpQuestionBinding): RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        fun bind(question: Help) {
            binding.textViewQuestion.text = question.question
            binding.textViewAnswer.text = question.answer

            binding.root.setOnClickListener {
                question.isExpanded = !question.isExpanded

                println(question.isExpanded)

                if (question.isExpanded) {
                    binding.textViewAnswer.visibility = View.VISIBLE
                } else {
                    binding.textViewAnswer.visibility = View.GONE
                }
            }
        }
    }
}