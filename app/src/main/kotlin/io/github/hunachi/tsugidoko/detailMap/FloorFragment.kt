package io.github.hunachi.tsugidoko.detailMap

import android.content.Context
import android.graphics.Matrix
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.graphics.translationMatrix
import androidx.core.os.bundleOf
import androidx.core.view.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.model.ClassRoom
import io.github.hunachi.tsugidoko.model.FloorRooms
import io.github.hunachi.tsugidoko.util.inflate
import kotlinx.android.synthetic.main.fragment_floor.view.*
import java.lang.Exception


class FloorFragment : Fragment() {

    private lateinit var floorRooms: FloorRooms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            floorRooms = it.getSerializable(ARG_FLOOR) as FloorRooms
        } ?: IllegalAccessException()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            container!!.inflate(R.layout.fragment_floor).apply {
                Picasso.get()
                        .load(floorRooms.imageUrl)
                        .fit()
                        .centerInside()
                        .into(mapImageView, object : Callback {
                            override fun onSuccess() {
                                frame.apply {
                                    floorRooms.rooms.forEach {
                                        createPeopleImageView(mapImageView, it)
                                    }
                                }
                            }

                            override fun onError(e: Exception?) {}

                        })
            }

    private fun View.createPeopleImageView(mapImageView: ImageView, classRoom: ClassRoom) {
        val imageView = ImageView(context).apply {
            val size = 30f
            val density = context.resources.displayMetrics.density
            val width = (size * density).toInt()
            val height = (size * density).toInt()
            layoutParams = FrameLayout.LayoutParams(width, height).apply {
                val mat = Array(9) { 0f }.toFloatArray()
                mapImageView.imageMatrix.getValues(mat)

                val mapTransX = mat[Matrix.MTRANS_X]
                val mapTransY = mat[Matrix.MTRANS_Y]

                // detail mapの画像内でのマーカーの位置[dp]
                // ImageViewの中央によっているので、2 * mapTransX、2 * mapTransYをそれぞれ引いておく
                val x = classRoom.detailPosition.x * (mapImageView.width - 2 * mapTransX)// / density
                val y= classRoom.detailPosition.y * (mapImageView.height - 2 * mapTransY)// / density

                val transX = mapImageView.marginLeft + mapTransX.toInt() + x.toInt()
                val transY = mapImageView.marginTop + mapTransY.toInt() + y.toInt()
                setMargins(transX, transY, 0, 0)
            }
        }
        Log.d("map", mapImageView.matrix.toString() + "     " + mapImageView.imageMatrix.toString())
        Picasso.get()
                .load(R.drawable.people)
                .fit()
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        Log.d("icon", imageView.matrix.toString() + "     " + imageView.imageMatrix.toString())
                    }

                    override fun onError(e: Exception?) {}

                })
        frame.addView(imageView)
    }

    companion object {
        const val ARG_FLOOR = "floor"

        fun newInstance(floorRooms: FloorRooms) = FloorFragment().apply {
            arguments = bundleOf(ARG_FLOOR to floorRooms)
        }
    }
}
