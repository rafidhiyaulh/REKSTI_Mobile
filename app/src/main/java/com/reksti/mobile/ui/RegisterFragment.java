package com.reksti.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.reksti.mobile.model.RegisterRequest;
import com.reksti.mobile.model.RegisterResponse;
import com.reksti.mobile.network.ApiClient;
import com.reksti.mobile.network.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 101;
    private EditText etNim, etNama;
    private ImageView ivPreview;
    private Bitmap selectedImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etNim = view.findViewById(R.id.et_nim);
        etNama = view.findViewById(R.id.et_nama);
        ivPreview = view.findViewById(R.id.iv_preview);
        Button btnSelectImage = view.findViewById(R.id.btn_capture);  // Tombol pilih foto
        Button btnRegister = view.findViewById(R.id.btn_register);

        btnSelectImage.setOnClickListener(v -> openGallery());
        btnRegister.setOnClickListener(v -> submitRegistration());

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                ivPreview.setImageBitmap(selectedImage);
            } catch (IOException e) {
                Toast.makeText(getContext(), "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitRegistration() {
        String nim = etNim.getText().toString().trim();
        String nama = etNama.getText().toString().trim();

        if (nim.isEmpty() || nama.isEmpty() || selectedImage == null) {
            Toast.makeText(getContext(), "Lengkapi semua field dan pilih gambar", Toast.LENGTH_SHORT).show();
            return;
        }

        String base64Image = encodeImageToBase64(selectedImage);

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        RegisterRequest request = new RegisterRequest(nim, nama, base64Image);

        Call<RegisterResponse> call = apiService.registerStudent(request);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    Toast.makeText(getContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Gagal registrasi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        // Resize jika terlalu besar
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 320, 320, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }
}
