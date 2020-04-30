package com.example.imagepicker.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.imagepicker.R
import kotlinx.android.synthetic.main.single_image.view.*

class ImagePickerAdapter(val items: MutableList<String>?, val context: Context) : RecyclerView.Adapter<ImagePickerAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagePickerAdapter.MyViewHolder {
//        TODO("Not yet implemented")

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_image, parent, false))
    }

    override fun getItemCount(): Int {
        if (items != null) {
            return items.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ImagePickerAdapter.MyViewHolder, position: Int) {
//        TODO("Not yet implemented")
        holder.imageView.setImageURI(Uri.parse(items?.get(position)))

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         var imageView: ImageView = TODO()

        init {
            imageView=itemView.imageViewId
        }


    }
}

//class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
//    // Holds the TextView that will add each animal to
//    val tvAnimalType = view.tv_animal_type
//}