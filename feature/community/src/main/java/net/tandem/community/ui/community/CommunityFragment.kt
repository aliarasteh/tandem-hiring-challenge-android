package net.tandem.community.ui.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import net.tandem.community.databinding.FragmentCommunityBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommunityFragment : Fragment() {

    private val viewModel by viewModels<CommunityViewModel>()

    private lateinit var binding: FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this.viewLifecycleOwner
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.communityResponse.observe(viewLifecycleOwner) {
            Log.i("test", "~~~~$it")
        }
    }

}