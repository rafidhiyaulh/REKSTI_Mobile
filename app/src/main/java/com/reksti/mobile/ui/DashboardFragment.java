package com.reksti.mobile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.reksti.mobile.R;
import com.reksti.mobile.model.StudentResponse;
import com.reksti.mobile.network.ApiClient;
import com.reksti.mobile.network.ApiService;
import com.reksti.mobile.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private TextView namaTextView, jumlahKelasTextView;

    public DashboardFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        namaTextView = view.findViewById(R.id.namaTextView);
        jumlahKelasTextView = view.findViewById(R.id.jumlahKelasTextView);
        Button btnRegisterFragment = view.findViewById(R.id.btn_register_fragment);

        // Navigasi ke RegisterFragment saat tombol diklik
        btnRegisterFragment.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        fetchStudentInfo();

        return view;
    }

    private void fetchStudentInfo() {
        String nim = SharedPrefManager.getInstance(requireContext()).getNIM();

        if (nim == null || nim.isEmpty()) {
            Toast.makeText(getContext(), "Silakan registrasi terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<StudentResponse> call = apiService.getStudent(nim);

        call.enqueue(new Callback<StudentResponse>() {
            @Override
            public void onResponse(@NonNull Call<StudentResponse> call,
                                   @NonNull Response<StudentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StudentResponse student = response.body();
                    namaTextView.setText("Nama: " + student.getNama());
                    int jumlahKelas = student.getClassUids() != null ? student.getClassUids().size() : 0;
                    jumlahKelasTextView.setText("Jumlah kelas: " + jumlahKelas);
                } else {
                    Toast.makeText(getContext(), "Gagal mengambil data mahasiswa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<StudentResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
