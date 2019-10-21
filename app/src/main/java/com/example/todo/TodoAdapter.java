package com.example.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<Todo> todoList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public TodoAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_to_do, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.bind(todoList.get(position));
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void addItem(Todo todo) {
        todoList.add(0, todo);
        notifyItemInserted(0);
    }

    public void updateItem(int position, Todo item) {
        todoList.set(position, item);
        notifyItemChanged(position);
    }

    public void addAll(List<Todo> todos) {
        todoList.clear();
        todoList.addAll(todos);
        notifyDataSetChanged();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDate;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDate = itemView.findViewById(R.id.textViewTime);
        }

        public void bind(final Todo item) {
            textViewTitle.setText(item.getTitle());
            textViewDate.setText(item.getDate().toString());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition(), item);
                    }
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position, Todo item);
    }
}
