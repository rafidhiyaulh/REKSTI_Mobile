package com.reksti.mobile.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.reksti.mobile.R;
import com.reksti.mobile.model.ClassCreateRequest;
import com.reksti.mobile.model.ClassCreateResponse;
import com.reksti.mobile.network.ApiClient;
import com.reksti.mobile.network.ApiService;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateClassFragment extends Fragment {

    private EditText etStudentList;
    private Button btnCreateClass, btnCopyUid;
    private TextView tvClassUid;

    public CreateClassFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_class, container, false);

        etStudentList = view.findViewById(R.id.et_student_list);
        btnCreateClass = view.findViewById(R.id.btn_create_class);
        tvClassUid = view.findViewById(R.id.tv_class_uid);
        btnCopyUid = view.findViewById(R.id.btn_copy_uid);

        btnCreateClass.setOnClickListener(v -> createClass());

        btnCopyUid.setOnClickListener(v -> {
            String uidText = tvClassUid.getText().toString().replace("Class UID: ", "").trim();
            if (!uidText.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Class UID", uidText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Class UID disalin ke clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void createClass() {
        String input = etStudentList.getText().toString().trim();

        if (TextUtils.isEmpty(input)) {
            Toast.makeText(getContext(), "Mohon masukkan daftar NIM", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> nimList = Arrays.asList(input.split("\\r?\\n"));
        ClassCreateRequest request = new ClassCreateRequest(nimList);

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<ClassCreateResponse> call = apiService.createClass(request);

        call.enqueue(new Callback<ClassCreateResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClassCreateResponse> call,
                                   @NonNull Response<ClassCreateResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    String classUid = response.body().getClassUid();
                    tvClassUid.setText("Class UID: " + classUid);
                    btnCopyUid.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Gagal membuat kelas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassCreateResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
