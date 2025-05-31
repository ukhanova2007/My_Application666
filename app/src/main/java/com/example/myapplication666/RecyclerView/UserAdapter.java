package com.example.myapplication666.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.myapplication666.R;
import com.example.myapplication666.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> usersList;

    public UserAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    // ViewHolder класс
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewSurname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewSurname = itemView.findViewById(R.id.textViewSurname);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fio_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Users user = usersList.get(position);
            String name = user.getName();
            String surname = user.getSurname();
            holder.textViewName.setText(name);
            holder.textViewSurname.setText(surname);
        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }

        public void setUsersList(List<Users> newUsersList) {
            this.usersList = newUsersList;
            notifyDataSetChanged(); // Важно: Обновляем RecyclerView после обновления данных
        }
    }