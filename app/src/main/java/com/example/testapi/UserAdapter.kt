package com.example.testapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testapi.api.User

class UserAdapter(private val users:ArrayList<User>,val onOptionClick:(user:User, view:View)->Unit,val onItemClick:(user:User)->Unit):
    RecyclerView.Adapter<UserAdapter.DVHolder>(){
    class DVHolder(iv: View):
        RecyclerView.ViewHolder(iv) {
        fun bind(user: User, onOptionClick:(user:User, view:View)->Unit, onItemClick:(user:User)->Unit) {
            itemView.apply {
                this.findViewById<TextView>(R.id.text1).text =user.name
                this.findViewById<TextView>(R.id.text2).text =user.email
                val button = this.findViewById<ImageButton>(R.id.btn)
                itemView.setOnClickListener {
                    onItemClick(user)
                }
                button.setOnClickListener {
                    onOptionClick(user,button)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DVHolder =
        DVHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item,parent,false))

    override fun getItemCount(): Int =users.size

    override fun onBindViewHolder(holder: DVHolder, position: Int) {
        holder.bind(users[position],onItemClick = this.onItemClick,onOptionClick = this.onOptionClick)
    }
    fun addUsers(users:List<User>) {
        this.users.apply {
            clear()
            addAll(users)
        }
    }

}