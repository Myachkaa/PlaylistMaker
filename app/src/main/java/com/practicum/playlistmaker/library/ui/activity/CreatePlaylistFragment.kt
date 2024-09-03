package com.practicum.playlistmaker.library.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlistmaker.library.ui.view_model.CreatePlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    private val createPlaylistViewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        createPlaylistViewModel.playlistCreated.observe(viewLifecycleOwner) { success ->
            if (success) {
                showCustomToast(
                    getString(
                        R.string.playlist_created_message,
                        binding.nameEditText.text.toString()
                    )
                )
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                showCustomToast(getString(R.string.failed_to_create_playlist))
            }
        }
    }

    private fun setupUI() {
        binding.backButton.setOnClickListener {
            handleBackNavigation()
        }

        binding.createButton.setOnClickListener {
            if (binding.nameEditText.text?.isNotEmpty() == true) {
                createPlaylist()
            }
        }

        binding.placeForCover.setOnClickListener {
            selectImage()
        }

        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {

                val nameIsEmpty = editable?.toString().isNullOrEmpty()
                binding.createButton.isEnabled = !nameIsEmpty

                if (nameIsEmpty) {
                    binding.createButton.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        )
                    )
                } else {
                    binding.createButton.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue
                        )
                    )
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun createPlaylist() {
        val name = binding.nameEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val coverImagePath = selectedImageUri?.toString()
        createPlaylistViewModel.createPlaylist(name, description, coverImagePath)
    }

    private fun selectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val savedUri =
                    createPlaylistViewModel.copyImageToPrivateStorage(requireContext(), uri)
                if (savedUri != null) {
                    selectedImageUri = savedUri
                    Glide.with(this)
                        .load(savedUri)
                        .apply(RequestOptions().override(1000, 1000))
                        .transform(
                            CenterCrop(), RoundedCorners(
                                TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    8F,
                                    resources.displayMetrics
                                ).toInt()
                            )
                        )
                        .into(binding.placeForCover)
                    binding.playerTrackArtwork.isVisible = false
                }
            }
        }
    }


    private fun showCustomToast(message: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)

        val text: TextView = layout.findViewById(R.id.toast_text)
        text.text = message

        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 100)
        toast.show()
    }


    private fun handleBackNavigation() {
        if (binding.nameEditText.text?.isNotEmpty() == true || selectedImageUri != null) {
            showExitConfirmationDialog()
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.exit_creation_title)
            .setMessage(R.string.exit_creation_message)
            .setPositiveButton(R.string.finish) { _, _ ->
                requireActivity().supportFragmentManager.popBackStack()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = CreatePlaylistFragment()
        private const val REQUEST_CODE_SELECT_IMAGE = 1
    }
}
