package com.reksti.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.reksti.mobile.R;
import com.reksti.mobile.model.AttendRequest;
import com.reksti.mobile.model.AttendResponse;
import com.reksti.mobile.network.ApiClient;
import com.reksti.mobile.network.ApiService;
import com.reksti.mobile.utils.SharedPrefManager;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsensiFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE_ABSEN = 1002;
    private EditText etClassUid;
    private ImageView ivPreview;
    private Button btnCapture, btnAttend;
    private Bitmap capturedImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_absensi, container, false);

        etClassUid = view.findViewById(R.id.et_class_uid);
        ivPreview = view.findViewById(R.id.iv_preview_absen);
        btnCapture = view.findViewById(R.id.btn_capture_absen);
        btnAttend = view.findViewById(R.id.btn_attend);

        btnCapture.setOnClickListener(v -> openCamera());

        btnAttend.setOnClickListener(v -> {
            if (capturedImage == null) {
                Toast.makeText(getContext(), "Silakan ambil foto terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            String nim = SharedPrefManager.getInstance(requireContext()).getNIM();
            String classUid = etClassUid.getText().toString().trim();
            String base64Image = bitmapToBase64(capturedImage);

            AttendRequest request = new AttendRequest(classUid, base64Image);
            ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
            Call<AttendResponse> call = apiService.attendClass(request);

            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<AttendResponse> call, @NonNull Response<AttendResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                        Toast.makeText(getContext(), "Absensi berhasil: " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Gagal melakukan absensi", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AttendResponse> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_ABSEN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE_ABSEN && resultCode == Activity.RESULT_OK && data != null) {
            capturedImage = (Bitmap) data.getExtras().get("data");
            ivPreview.setImageBitmap(capturedImage);
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }
}
