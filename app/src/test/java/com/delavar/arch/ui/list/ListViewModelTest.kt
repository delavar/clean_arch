package com.delavar.arch.ui.list

import com.delavar.arch.domain.usecase.PlaceListUseCase
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.delavar.arch.TestLifecycle
import com.delavar.arch.domain.model.Place
import com.delavar.arch.domain.response.ErrorResponse
import com.delavar.arch.domain.response.SuccessResponse
import com.delavar.arch.domain.response.UseCaseResponse
import com.delavar.arch.domain.response.ErrorModel
import com.delavar.arch.domain.response.ErrorStatus
import com.nhaarman.mockitokotlin2.*
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.MockitoAnnotations


class ListViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: ListViewModel;
    @Mock
    lateinit var placeListUseCase: PlaceListUseCase

    val lifecycle=TestLifecycle.initialized()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ListViewModel(placeListUseCase)

        lifecycle.start()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun isLoading() {

        val list:List<Place> = emptyList()
        val resultCaptor= argumentCaptor<((UseCaseResponse<List<Place>>)->Unit)>()
        whenever(placeListUseCase.setParameters(any(), any(), any(), any())).thenReturn(placeListUseCase)
        viewModel.setLocation(22.0, 32.0)
        verify(placeListUseCase).execute(eq(viewModel.compositeDisposable),resultCaptor.capture(), any())
        resultCaptor.firstValue.invoke(SuccessResponse(list))

        assertThat(viewModel.isLoading.value, `is`(false))

    }

    @Test
    fun getError() {
        val errorModel=ErrorModel(ErrorStatus.BAD_RESPONSE,"bad response")
        val resultCaptor= argumentCaptor<((UseCaseResponse<List<Place>>)->Unit)>()
        whenever(placeListUseCase.setParameters(any(), any(), any(), any())).thenReturn(placeListUseCase)

        viewModel.setLocation(22.0, 32.0)

        verify(placeListUseCase).execute(eq(viewModel.compositeDisposable),resultCaptor.capture(), any())
        resultCaptor.firstValue.invoke(ErrorResponse(errorModel))


        val observer = mock<Observer<ErrorModel>>()
        viewModel.error.observe(lifecycle,observer)
        verify(observer).onChanged(eq(errorModel))
    }

    @Test
    fun setLocation() {
        val place1=Place("1","yara company")
        val place2=Place("2","azadi")
        val list:List<Place> = arrayListOf(place1,place2)

        val resultCaptor= argumentCaptor<((UseCaseResponse<List<Place>>)->Unit)>()
        whenever(placeListUseCase.setParameters(any(), any(), any(), any())).thenReturn(placeListUseCase)

        viewModel.setLocation(22.0, 32.0)

        verify(placeListUseCase).execute(eq(viewModel.compositeDisposable),resultCaptor.capture(), any())
        resultCaptor.firstValue.invoke(SuccessResponse(list))

        val observer=mock<(List<Place>?)->Unit>()
        viewModel.places.observe(lifecycle,observer)
        verify(observer).invoke(list)
    }

    @Test
    fun loadMore() {
        val place1=Place("1","yara company")
        val place2=Place("2","azadi")
        val list:List<Place> = arrayListOf(place1,place2)

        val place3=Place("3","place 3")
        val list1:List<Place> = arrayListOf(place3)


        val resultCaptor= argumentCaptor<((UseCaseResponse<List<Place>>)->Unit)>()
        whenever(placeListUseCase.setParameters(any(), any(), any(), any())).thenReturn(placeListUseCase)
        viewModel.limit=2
        viewModel.setLocation(22.0, 32.0)
        verify(placeListUseCase).execute(eq(viewModel.compositeDisposable),resultCaptor.capture(), any())
        resultCaptor.firstValue.invoke(SuccessResponse(list))

        viewModel.loadMore()
        verify(placeListUseCase, times(2)).execute(eq(viewModel.compositeDisposable),resultCaptor.capture(), any())
        resultCaptor.firstValue.invoke(SuccessResponse(list1))

        val offsetCaptor= argumentCaptor<Int>()
        verify(placeListUseCase, times(2)).setParameters(any(), any(), eq(2),offsetCaptor.capture())


        assertEquals(3, viewModel.places.value.size)
        assertEquals(2, offsetCaptor.lastValue)
        assertEquals(3, viewModel.offset)
    }
}