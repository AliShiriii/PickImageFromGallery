package com.example.pickimagefromgallery.utils

object Contracts {

    // Image pick code
    const val IMAGE_GALLERY_CODE = 1000

    const val IMAGE_CAMERA_CODE = 1001

    //Permission code
    const val PERMISSION_CODE = 2001

}


//    private fun showPictureDialog() {
//        val pictureDialog = AlertDialog.Builder(requireContext())
//        pictureDialog.setTitle("Select Action")
//        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
//        pictureDialog.setItems(
//            pictureDialogItems
//        ) { dialog, which ->
//            when (which) {
//                0 -> choosePhotoFromGallery()
//                1 -> takePhotoFromCamera()
//            }
//        }
//        pictureDialog.show()
//    }