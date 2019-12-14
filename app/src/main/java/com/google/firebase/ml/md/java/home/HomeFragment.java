package com.google.firebase.ml.md.java.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.ml.md.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View HomeView;
    private RecyclerView myDataList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter<ShowData, ShowData_ViewHolder> firestoreRecyclerAdapter;

    public HomeFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        myDataList = (RecyclerView) root.findViewById(R.id.DataList);
        myDataList.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = db.collection("goodsStatus");

        try {
            FirestoreRecyclerOptions<ShowData> response = new FirestoreRecyclerOptions.Builder<ShowData>()
                    .setQuery(query, ShowData.class)
                    .build();

            firestoreRecyclerAdapter =
                    new FirestoreRecyclerAdapter<ShowData, ShowData_ViewHolder>(
                            response
                    ) {
                        @Override
                        protected void onBindViewHolder(ShowData_ViewHolder holder, int position, ShowData model) {
                            holder.package_id.setText("Package ID: " + model.getBarang_id());
                            holder.name.setText("Scanner: " + model.getNama());
                            holder.loc.setText("Location: " + model.getLokasi());
                        }

                        @NonNull
                        @Override
                        public ShowData_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                            View view = LayoutInflater.from(viewGroup.getContext())
                                    .inflate(R.layout.row, viewGroup, false);

                            return new ShowData_ViewHolder(view);

                        }
                    };
            myDataList.setAdapter(firestoreRecyclerAdapter);
        } catch (Exception e) {
            //  Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                getActivity().finish();
            }
        };
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        firestoreRecyclerAdapter.startListening();
    }

}
