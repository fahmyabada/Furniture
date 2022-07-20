package com.example.furniture.presentation.ui.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.furniture.R
import com.example.furniture.data.api.SessionManager
import com.example.furniture.data.model.furniture_nearby.Data
import com.example.furniture.data.model.home.Category
import com.example.furniture.data.model.home.Discount
import com.example.furniture.data.model.home.Offer
import com.example.furniture.data.model.home.Save
import com.example.furniture.data.util.Resource
import com.example.furniture.databinding.FragmentHomeBinding
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var layoutManager1: LinearLayoutManager
    private lateinit var layoutManager2: LinearLayoutManager
    private lateinit var layoutManager3: LinearLayoutManager

    @Inject
    lateinit var adapterValueCategory: CustomHolderCategory

    @Inject
    lateinit var adapterValueOffersSaves: CustomHolderOffersSaves

    @Inject
    lateinit var adapterValueDiscount: CustomHolderDiscount

    @Inject
    lateinit var adapterValueSaves: CustomHolderSaves

    @Inject
    lateinit var adapterValueFurnitureNearby: CustomHolderFurnitureNearby

    var typeOffersSaves = "unReverse"
    var typeSaves = "unReverse"
    var typeDiscount = "unReverse"

    private lateinit var someActivityPermissionLauncher: ActivityResultLauncher<String>

    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e.,
    // how often you should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient
    // has a new Location
    private lateinit var locationCallback: LocationCallback

    // This will store current location info
    private var currentLocation: Location? = null
    private lateinit var longitude: String
    private lateinit var latitude: String

    private val data: MutableList<Data> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        sessionManager = SessionManager(requireContext())
        sessionManager.setView(requireActivity())

        initRecyclerView()

        someActivityPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result: Boolean ->
            if (result) {

                Log.i("", "ACCESS_FINE_LOCATION 11**** ")
                currentLocation()

                if (!isLocationEnable()) {
                    showMessageOk("انت تحتاج الي تفعيل الموقع.. ") { _, _ ->
                        val intent = Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        )
                        startActivity(intent)
                        return@showMessageOk
                    }
                }

            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Log.i("", "ACCESS_FINE_LOCATION 22**** ")
                        showMessageOk("انت تحتاج الي تفعيل صلاحية الموقع حتي تستطيع التكملة.. ") { _, _ ->
                            someActivityPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                            return@showMessageOk
                        }
                    } else {
                        Log.i("", "ACCESS_FINE_LOCATION 33**** ")

                        val oKClick = { dialog: DialogInterface, _: Int ->
                            if (!checkPermission()) {
                                requestPermission()
                            }
                            dialog.dismiss()
                        }

                        // this (_) write if not used to clean code
                        val settingClick = { dialog: DialogInterface, _: Int ->
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts(
                                    "package",
                                    requireActivity().packageName,
                                    null
                                )
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            dialog.dismiss()
                        }

                        showMessageOkAndSetting(
                            oKClick,
                            settingClick,
                            "يجب تفعيل صلاحية الموقع.. \n\n عليك بالذهاب الي اعدادات التطبيق وتفعيلها يدويا.. "
                        )

                    }
                }
            }
        }

        if (!checkPermission()) {
            requestPermission()
        } else {
            Log.i("", "shouldShowRequestPermissionRationale 66**** ")
            if (!isLocationEnable()) {
                showMessageOk("انت تحتاج الي تفعيل الموقع.. ") { _, _ ->
                    val intent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    startActivity(intent)
                    return@showMessageOk
                }
            }
        }

    }

    private fun initRecyclerView() {
        layoutManager1 =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        layoutManager2 =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        layoutManager3 =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)


        binding.recCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = adapterValueCategory
        }

        adapterValueCategory.setOnItemClickListener { item: Category ->
            Log.i("", "setOnItemClickListener category**** ")
        }

        binding.recOfferSaves.apply {
            layoutManager = layoutManager1

            adapter = adapterValueOffersSaves
        }

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recOfferSaves)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (typeOffersSaves == "unReverse") {
                    if (layoutManager1.findLastCompletelyVisibleItemPosition() < (adapterValueOffersSaves.itemCount - 1)) {
                        layoutManager1.smoothScrollToPosition(
                            binding.recOfferSaves,
                            RecyclerView.State(),
                            layoutManager1.findLastCompletelyVisibleItemPosition() + 1
                        )
                    } else if (layoutManager1.findLastCompletelyVisibleItemPosition() == (adapterValueOffersSaves.itemCount - 1) && adapterValueOffersSaves.itemCount != 0) {
                        typeOffersSaves = "Reverse"
                        layoutManager1.smoothScrollToPosition(
                            binding.recOfferSaves,
                            RecyclerView.State(),
                            layoutManager1.findLastCompletelyVisibleItemPosition() - 1
                        )
                    }
                } else {
                    if (layoutManager1.findLastCompletelyVisibleItemPosition() == 0) {
                        typeOffersSaves = "unReverse"
                        layoutManager1.smoothScrollToPosition(
                            binding.recOfferSaves,
                            RecyclerView.State(),
                            layoutManager1.findLastCompletelyVisibleItemPosition() + 1
                        )
                    } else {
                        layoutManager1.smoothScrollToPosition(
                            binding.recOfferSaves,
                            RecyclerView.State(),
                            layoutManager1.findLastCompletelyVisibleItemPosition() - 1
                        )
                    }
                }
            }
        }, 0, 3000)

        adapterValueOffersSaves.setOnItemClickListener { item: Offer ->
            Log.i("", "setOnItemClickListener Offer saves**** ")

        }

        binding.recSaves.apply {
            layoutManager = layoutManager2

            adapter = adapterValueSaves
        }

        val snapHelper2 = LinearSnapHelper()
        snapHelper2.attachToRecyclerView(binding.recSaves)

        val timer2 = Timer()
        timer2.schedule(object : TimerTask() {
            override fun run() {
                if (typeSaves == "unReverse") {
                    if (layoutManager2.findLastCompletelyVisibleItemPosition() < (adapterValueSaves.itemCount - 1)) {
                        layoutManager2.smoothScrollToPosition(
                            binding.recSaves,
                            RecyclerView.State(),
                            layoutManager2.findLastCompletelyVisibleItemPosition() + 1
                        )
                    } else if (layoutManager2.findLastCompletelyVisibleItemPosition() == (adapterValueSaves.itemCount - 1) && adapterValueSaves.itemCount != 0) {
                        typeSaves = "Reverse"
                        layoutManager2.smoothScrollToPosition(
                            binding.recSaves,
                            RecyclerView.State(),
                            layoutManager2.findLastCompletelyVisibleItemPosition() - 1
                        )
                    }
                } else {
                    if (layoutManager2.findLastCompletelyVisibleItemPosition() == 0) {
                        typeSaves = "unReverse"
                        layoutManager2.smoothScrollToPosition(
                            binding.recSaves,
                            RecyclerView.State(),
                            layoutManager2.findLastCompletelyVisibleItemPosition() + 1
                        )
                    } else {
                        layoutManager2.smoothScrollToPosition(
                            binding.recSaves,
                            RecyclerView.State(),
                            layoutManager2.findLastCompletelyVisibleItemPosition() - 1
                        )
                    }
                }
            }
        }, 0, 3500)

        adapterValueSaves.setOnItemClickListener { item: Save ->
            Log.i("", "setOnItemClickListener saves**** ")
        }

        binding.recDiscount.apply {
            layoutManager = layoutManager3

            adapter = adapterValueDiscount
        }

        val snapHelper3 = LinearSnapHelper()
        snapHelper3.attachToRecyclerView(binding.recDiscount)

        val timer3 = Timer()
        timer3.schedule(object : TimerTask() {
            override fun run() {
                if (typeDiscount == "unReverse") {
                    if (layoutManager3.findLastCompletelyVisibleItemPosition() < (adapterValueDiscount.itemCount - 1)) {
                        layoutManager3.smoothScrollToPosition(
                            binding.recDiscount,
                            RecyclerView.State(),
                            layoutManager3.findLastCompletelyVisibleItemPosition() + 1
                        )
                    } else if (layoutManager3.findLastCompletelyVisibleItemPosition() == (adapterValueDiscount.itemCount - 1) && adapterValueDiscount.itemCount != 0) {
                        typeDiscount = "Reverse"
                        layoutManager3.smoothScrollToPosition(
                            binding.recDiscount,
                            RecyclerView.State(),
                            layoutManager3.findLastCompletelyVisibleItemPosition() - 1
                        )
                    }
                } else {
                    if (layoutManager3.findLastCompletelyVisibleItemPosition() == 0) {
                        typeDiscount = "unReverse"
                        layoutManager3.smoothScrollToPosition(
                            binding.recDiscount,
                            RecyclerView.State(),
                            layoutManager3.findLastCompletelyVisibleItemPosition() + 1
                        )
                    } else {
                        layoutManager3.smoothScrollToPosition(
                            binding.recDiscount,
                            RecyclerView.State(),
                            layoutManager3.findLastCompletelyVisibleItemPosition() - 1
                        )
                    }
                }
            }
        }, 0, 4000)

        adapterValueDiscount.setOnItemClickListener { item: Discount ->
            Log.i("", "setOnItemClickListener Discount**** ")
        }

        binding.recFurnitureNearby.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = adapterValueFurnitureNearby
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && dy > 0) {
                        val map = HashMap<String, String>()
                        map["page"] = viewModel.numPageViewModel.value!!
                        viewModel.getFurnitureNearby(map)
                    }
                }
            })
        }

        adapterValueFurnitureNearby.setOnItemClickListener { item: Data ->
            Log.i("", "setOnItemClickListener FurnitureNearby**** ")
        }

        displayItems()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun displayItems() {
        viewModel.getHome()
        viewModel.homeItem.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        if (it.status) {
                            Log.i("Success", "category********* ${it.data.categories}")
                            if (it.data.categories.isNotEmpty()) {
                                adapterValueCategory.differ.submitList(it.data.categories)
                                adapterValueOffersSaves.differ.submitList(it.data.offers)
                                adapterValueSaves.differ.submitList(it.data.saves)
                                adapterValueDiscount.differ.submitList(it.data.discounts)
                            } else
                                Toast.makeText(
                                    requireContext(),
                                    "لا يوجد اصناف",
                                    Toast.LENGTH_LONG
                                ).show()

                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(
                            context,
                            "An error occurred in home $it",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.i("", "error getHome********* $it")
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun currentLocation() {
        locationRequest = LocationRequest.create().apply {
            // Sets the desired interval for
            // active location updates.
            // This interval is inexact.
            interval = TimeUnit.SECONDS.toMillis(5)

            // Sets the fastest rate for active location updates.
            // This interval is exact, and your application will never
            // receive updates more frequently than this value
            fastestInterval = TimeUnit.SECONDS.toMillis(5)

            // Sets the maximum time when batched location
            // updates are delivered. Updates may be
            // delivered sooner than this interval
            //            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation.let {
                    currentLocation = it
                    latitude = currentLocation!!.latitude.toString()
                    longitude = currentLocation!!.longitude.toString()
                    // use latitude and longitude as per your need
                    Log.i("longitude fused", "*********** $longitude")
                    Log.i("latitude fused", "*********** $latitude")
                    val map = HashMap<String, String>()
                    map["lng"] = longitude
                    map["lat"] = latitude
                    viewModel.getFurnitureNearby(map)
                }
            }
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )

        viewModel.furnitureNearbyItem.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        if (it.status) {
                            Log.i("Success", "furnitureNearbyItem********* ${it.data}")

                            if (viewModel.numPageViewModel.value?.toInt()!! <= it.paginator.lastPage) {
                                viewModel.numPageViewModel.postValue((viewModel.numPageViewModel.value!!.toInt() + 1).toString())
                            }


                            if (it.data.isNotEmpty()) {
                                data.addAll(it.data)
                            }

                            viewModel.setData.postValue(data)


                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(
                            context,
                            "An error occurred in furnitureNearbyItem $it",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.i("", "error furnitureNearbyItem********* $it")
                    }
                }
                is Resource.Loading -> {
                }
            }
        }

        //setData
        viewModel.setData.observe(viewLifecycleOwner) {
            try {
                if (data.isEmpty()) {
                    data.addAll(it)
                }
                Log.i("Success", "checkFirstTimeOrder********* ${it.size}")

                adapterValueFurnitureNearby.setData(it)
            } catch (e: Exception) {
                Log.i("errorCatch", "setData********* ${e.message}")
            }
        }

    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        someActivityPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun showMessageOk(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("موافق", okListener)
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showMessageOkAndSetting(
        okListener: DialogInterface.OnClickListener,
        settingListener: DialogInterface.OnClickListener,
        message: String
    ) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("موافق", okListener)
            .setNegativeButton("اعدادات", settingListener)
            .setCancelable(false)
            .create()
            .show()
//        dialogCreateOkAndSetting = dialogBuilder.create()
//        dialogCreateOkAndSetting.show()
    }

    private fun isLocationEnable(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}