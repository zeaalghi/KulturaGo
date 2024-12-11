package com.dicoding.picodiploma.kulturago.ui.home.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.kulturago.R
import com.dicoding.picodiploma.kulturago.data.repository.model.MostVisitedCulturalPlace
import com.dicoding.picodiploma.kulturago.databinding.CarouselItemBinding
import com.dicoding.picodiploma.kulturago.ui.home.DetailsActivity

class CarouselAdapter(
    private val places: List<MostVisitedCulturalPlace>,
) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {

        val binding =
            CarouselItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
    }

    override fun getItemCount(): Int = places.size

    inner class CarouselViewHolder(private val binding: CarouselItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(place: MostVisitedCulturalPlace) {

            Glide.with(itemView.context)
                .load(place.image_link)
                .into(binding.carouselImage)


            binding.carouselHeading.text = place.nama


            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra("PLACE_ID", place.no)
                context.startActivity(intent)
            }
        }
    }
}
