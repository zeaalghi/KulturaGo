package com.dicoding.picodiploma.kulturago.ui.home.ui.home

import android.content.Context
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.picodiploma.kulturago.R
import com.dicoding.picodiploma.kulturago.data.repository.FirebaseRepository
import com.dicoding.picodiploma.kulturago.data.repository.model.MostVisitedCulturalPlace
import com.dicoding.picodiploma.kulturago.data.repository.model.RecommendedPlace
import com.dicoding.picodiploma.kulturago.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")

    private lateinit var firebaseAuth: FirebaseRepository
    private lateinit var carouselHandler: Handler
    private lateinit var carouselRunnable: Runnable

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private lateinit var savePreferencesButton: Button
    private lateinit var userPreferenceCard: View
    private lateinit var radioGroup: RadioGroup
    private var userId: String? = null

    private lateinit var listAdapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseAuth = FirebaseRepository(requireContext())
        userId = firebaseAuth.getCurrentUserId()

        setupRecyclerView()

        homeViewModel.recommendedPlaces.observe(viewLifecycleOwner) { recommendedPlaces ->
            recommendedPlaces?.let {
                updateRecyclerView(it)
            }
        }

        homeViewModel.fetchMostVisitedCulturalPlaces()
        homeViewModel.mostVisitedPlaces.observe(viewLifecycleOwner) { places ->
            places?.let {
                setupCarousel(it)
            }
        }

        radioGroup = binding.rgTourismPreferences
        savePreferencesButton = binding.savePreferencesButton
        userPreferenceCard = binding.userPreferenceCard

        applyBlurBehindCard()
        checkUserPreferences()

        val preferenceButton = binding.preferenceButton
        preferenceButton.setOnClickListener {
            toggleUserPreferenceCard()
        }

        savePreferencesButton.setOnClickListener {
            saveUserPreferences()
        }

        return root
    }

    private fun setupRecyclerView() {
        listAdapter = ListAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = listAdapter
    }

    private fun setupCarousel(places: List<MostVisitedCulturalPlace>) {
        val carouselAdapter = CarouselAdapter(places)
        binding.imageCarousel.adapter = carouselAdapter

        TabLayoutMediator(binding.indicator, binding.imageCarousel) { _, _ -> }.attach()

        carouselHandler = Handler(Looper.getMainLooper())
        carouselRunnable = object : Runnable {
            override fun run() {
                if (_binding == null) {
                    return
                }

                val currentItem = binding.imageCarousel.currentItem
                val itemCount = binding.imageCarousel.adapter?.itemCount ?: 0

                if (itemCount > 0) {
                    if (currentItem == itemCount - 1) {
                        binding.imageCarousel.setCurrentItem(0, false)
                    } else {
                        val nextItem = currentItem + 1
                        binding.imageCarousel.setCurrentItem(nextItem, true)
                    }
                }

                carouselHandler.postDelayed(this, 3000)
            }
        }

        carouselHandler.postDelayed(carouselRunnable, 3000)

        binding.imageCarousel.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        carouselHandler.removeCallbacks(carouselRunnable)
                    }

                    ViewPager2.SCROLL_STATE_IDLE -> {
                        carouselHandler.removeCallbacks(carouselRunnable)
                        carouselHandler.postDelayed(carouselRunnable, 3000)
                    }
                }
            }
        })
    }

    private fun toggleUserPreferenceCard() {
        if (userPreferenceCard.visibility == View.VISIBLE) {
            userPreferenceCard.visibility = View.GONE
            binding.blurFrameLayout.visibility = View.GONE
        } else {
            userPreferenceCard.visibility = View.VISIBLE
            binding.blurFrameLayout.visibility = View.VISIBLE
        }
        applyBlurBehindCard()
    }

    private fun applyBlurBehindCard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (userPreferenceCard.visibility == View.VISIBLE) {
                val renderEffect = RenderEffect.createBlurEffect(10f, 10f, Shader.TileMode.DECAL)
                binding.blurFrameLayout.setRenderEffect(renderEffect)
                binding.blurFrameLayout.visibility = View.VISIBLE
            } else {
                binding.blurFrameLayout.setRenderEffect(null)
                binding.blurFrameLayout.visibility = View.GONE
            }
        } else {
            binding.blurFrameLayout.setBackgroundColor(Color.parseColor("#80000000"))
            binding.blurFrameLayout.visibility =
                if (userPreferenceCard.visibility == View.VISIBLE) View.VISIBLE else View.GONE
        }
    }

    private fun checkUserPreferences() {
        val sharedPreferences =
            requireContext().getSharedPreferences("UserPreferences_$userId", Context.MODE_PRIVATE)
        val selectedPreference = sharedPreferences.getString("selectedPreference", null)

        if (selectedPreference != null) {
            binding.userPreferenceCard.visibility = View.GONE
            binding.blurFrameLayout.visibility = View.GONE
            getRecommendedPlaces(selectedPreference)
        } else {
            binding.userPreferenceCard.visibility = View.VISIBLE
            applyBlurBehindCard()
        }
    }

    private fun getRecommendedPlaces(keyword: String) {
        homeViewModel.fetchRecommendedPlaces(keyword)

        homeViewModel.recommendedPlaces.removeObservers(viewLifecycleOwner)
        homeViewModel.recommendedPlaces.observe(viewLifecycleOwner, { recommendedPlaces ->
            recommendedPlaces?.let {
                updateRecyclerView(it)
            }
        })
    }

    private fun updateRecyclerView(recommendedPlaces: List<RecommendedPlace>) {
        val listItems = recommendedPlaces.map {
            ListItem(
                id = it.id,
                imageUrl = it.image_link,
                title = it.place_name,
                description = it.description
            )
        }

        listAdapter = ListAdapter(listItems)
        binding.recyclerView.adapter = listAdapter
    }

    private fun saveUserPreferences() {
        val selectedPreference = when (radioGroup.checkedRadioButtonId) {
            R.id.rb_beach -> "Beach"
            R.id.rb_nature -> "Nature"
            R.id.rb_artificial -> "Artificial"
            R.id.rb_history -> "History"
            R.id.rb_culture -> "Culture"
            R.id.rb_agro -> "Agro"
            R.id.rb_village -> "Village"
            else -> null
        }

        if (selectedPreference != null) {
            val sharedPreferences =
                requireContext().getSharedPreferences("UserPreferences_$userId", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("selectedPreference", selectedPreference)
                apply()
            }

            userPreferenceCard.visibility = View.GONE
            binding.blurFrameLayout.visibility = View.GONE
            Toast.makeText(requireContext(), "Preferences saved!", Toast.LENGTH_SHORT).show()

            getRecommendedPlaces(selectedPreference)
        } else {
            Toast.makeText(requireContext(), "Please select a preference!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        carouselHandler.removeCallbacks(carouselRunnable)
    }
}



