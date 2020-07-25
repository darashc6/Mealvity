package cenec.mealvity.mealvityforowners.features.reservationlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.mealvity.mealvityforowners.R
import cenec.mealvity.mealvityforowners.core.reservation.Reservation
import cenec.mealvity.mealvityforowners.databinding.ItemListReservationBinding

/**
 * RecyclerView adpater binding a list of reservations
 * @param reservationList List of reservations
 */
class ReservationListRecyclerViewAdapter(private var reservationList: ArrayList<Reservation>): RecyclerView.Adapter<ReservationListRecyclerViewAdapter.ReservationViewHolder>() {
    private var rvListener: ReservationListRecyclerViewAdapterListener? = null // Listener for the RecyclerView adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val _binding = ItemListReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(_binding)
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(reservationList[position])
    }

    /**
     * Sets a new listener for the RecyclerView adapter
     * @param newListener New listener for the adapter
     */
    fun setReservationListRecyclerViewAdapterListener(newListener: ReservationListRecyclerViewAdapterListener) {
        rvListener = newListener
    }

    /**
     * Sets a new list of reservations
     * @param newReservationList New list of reservations
     */
    fun setReservationList(newReservationList: ArrayList<Reservation>) {
        reservationList = newReservationList
        notifyDataSetChanged()
    }

    inner class ReservationViewHolder(_binding: ItemListReservationBinding): RecyclerView.ViewHolder(_binding.root) {
        private val binding = _binding
        private var isRejectedReasonLayoutExpanded = false

        /**
         * Binds the Reservation to the itemview
         * @param reservation Reservation to bind
         */
        fun bind(reservation: Reservation) {
            binding.textViewReferenceNumber.text = "Reference Nº: ${reservation.referenceNumber}"
            binding.textViewDate.text = "Date: ${reservation.date}"
            binding.textViewTime.text = "Time: ${reservation.time}"
            binding.textViewNumberGuests.text = "Nº Guest(s): ${reservation.nGuests}"
            binding.textViewStatus.text = "${reservation.reservationStatus}"
            binding.textViewUserName.text = reservation.user!!.fullName
            binding.textViewUserPhoneNumber.text = if (reservation.user!!.phoneNumber.isNullOrEmpty()) "Not available" else reservation.user!!.phoneNumber
            binding.textViewUserEmail.text = reservation.user!!.email

            checkStatus(reservation)

            binding.buttonAccept.buttonAccept.setOnClickListener {
                rvListener?.onOptionSelected(layoutPosition, Reservation.ReservationStatus.ACCEPTED, "")
            }

            binding.buttonReject.buttonReject.setOnClickListener {
                isRejectedReasonLayoutExpanded = !isRejectedReasonLayoutExpanded
                expandRejectionMessageLayout()
            }

            binding.buttonConfirmRejection.buttonConfirmRejection.setOnClickListener {
                val textReason = binding.editTextRejectionReason.text.toString()
                if (textReason.isNotEmpty()) {
                    rvListener?.onOptionSelected(layoutPosition, Reservation.ReservationStatus.REJECTED, textReason)
                    isRejectedReasonLayoutExpanded = false
                    expandRejectionMessageLayout()
                } else {
                    binding.editTextRejectionReason.requestFocus()
                    binding.editTextRejectionReason.error = binding.root.context.getString(R.string.text_error_field_is_empty)
                }
            }
        }

        /**
         * Expands the layout featuring the reason for the REJECTED status
         */
        private fun expandRejectionMessageLayout() {
            if (!isRejectedReasonLayoutExpanded) {
                binding.layoutRejectionReason.visibility = View.GONE
            } else {
                binding.layoutRejectionReason.visibility = View.VISIBLE
            }
        }

        /**
         * Checks the status of the reservations. It will display diffetent layout depending on the result
         * @param reservation Reservation to check
         */
        private fun checkStatus(reservation: Reservation) {
            when (reservation.reservationStatus) {
                Reservation.ReservationStatus.ACCEPTED, Reservation.ReservationStatus.REJECTED -> {
                    binding.layoutUserDetails.visibility = View.GONE
                }
                else -> {
                    binding.layoutUserDetails.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * Interface for the RecyclerView
     */
    interface ReservationListRecyclerViewAdapterListener {
        /**
         * Triggered when the reservation option is selected (ACCEPTED or REJECTED)
         * @param position item position
         * @param updatedStatus New status of the reservation
         * @param reason Reason in case the status is REJECTED
         */
        fun onOptionSelected(position: Int, updatedStatus: Reservation.ReservationStatus, reason: String)
    }
}