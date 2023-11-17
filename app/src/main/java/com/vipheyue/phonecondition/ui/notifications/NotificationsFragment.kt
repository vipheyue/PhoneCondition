package com.vipheyue.phonecondition.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vipheyue.phonecondition.R
import com.vipheyue.phonecondition.databinding.FragmentNotificationsBinding
import com.vipheyue.phonecondition.ui.MyApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

	private var _binding: FragmentNotificationsBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val notificationsViewModel =
			ViewModelProvider(this).get(NotificationsViewModel::class.java)

		_binding = FragmentNotificationsBinding.inflate(inflater, container, false)
		val root: View = binding.root


		root.findViewById<Button>(R.id.button).setOnClickListener {
			GlobalScope.launch {
				MyApp.appDB.screenDao().deleteAllData()
			}
			Toast.makeText(requireActivity(), "已删除", Toast.LENGTH_SHORT).show();
		}

		return root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}