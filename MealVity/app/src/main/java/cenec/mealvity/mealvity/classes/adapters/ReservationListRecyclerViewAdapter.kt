package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ItemListReservationBinding
import cenec.mealvity.mealvity.classes.reservations.Reservation

class ReservationListRecyclerViewAdapter(private var reservationList: ArrayList<Reservation>): RecyclerView.Adapter<ReservationListRecyclerViewAdapter.ReservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val _binding = ItemListReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(reservationList[position])
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    fun setReservationList(newReservationList: ArrayList<Reservation>) {
        reservationList = newReservationList
        notifyDataSetChanged()
    }

    class ReservationViewHolder(_binding: ItemListReservationBinding): RecyclerView.ViewHolder(_binding.root) {
        private val binding = _binding

        fun bind(reservation: Reservation) {
            binding.textViewReferenceNumber.text = "Reference Nº: ${reservation.referenceNumber}"
            binding.textViewRestaurantName.text = "Restaurant: ${reservation.restaurantName}"
            binding.textViewDate.text = "Date: ${reservation.date}"
            binding.textViewTime.text = "Time: ${reservation.time}"
            binding.textViewNumberGuests.text = "Nº Guests: ${reservation.nGuests}"
            binding.textViewStatus.text = "${reservation.reservationStatus}"

            if (reservation.reservationStatus == Reservation.ReservationStatus.ACCEPTED) {
                binding.textViewStatus.setTextColor(binding.root.context.getColor(R.color.green_accept))
            } else if (reservation.reservationStatus == Reservation.ReservationStatus.REJECTED) {
                binding.textViewStatus.setTextColor(binding.root.context.getColor(R.color.red_reject))
            }

            binding.root.setOnClickListener {
                if (reservation.reservationStatus == Reservation.ReservationStatus.REJECTED) {
                    binding.textViewRejectionReason.text = "Reason: ${reservation.rejectionReason}"
                    if (binding.layoutRejectionReason.visibility == View.GONE) {
                        binding.layoutRejectionReason.visibility = View.VISIBLE
                    } else {
                        binding.layoutRejectionReason.visibility = View.GONE
                    }
                }
            }
        }
    }
}