package com.dicoding.picodiploma.kulturago.ui.home.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.kulturago.R
import com.dicoding.picodiploma.kulturago.databinding.ListItemBinding
import com.dicoding.picodiploma.kulturago.ui.home.DetailsActivity

class ListAdapter(
    private val items: List<ListItem>
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListItem) {
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .into(binding.itemImage)

            binding.itemTitle.text = item.title
            binding.itemDesc.text = item.description

            binding.itemDesc.maxLines = 2
            binding.itemDesc.ellipsize = android.text.TextUtils.TruncateAt.END

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra("PLACE_ID", item.id)
                context.startActivity(intent)
            }
        }
    }
}

data class ListItem(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val description: String
)
