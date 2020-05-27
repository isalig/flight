package io.aiico.flight.presentation.search

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.aiico.flight.R
import io.aiico.flight.ServiceLocator
import io.aiico.flight.domain.model.Destination
import io.aiico.flight.hideKeyboard
import io.aiico.flight.presentation.base.BaseDialogFragment
import io.aiico.flight.presentation.search.list.DestinationsAdapter
import io.aiico.flight.presentation.utils.TextChangeAdapter
import io.aiico.flight.toast
import kotlinx.android.synthetic.main.fragment_search.*

class SearchDialog : BaseDialogFragment<SearchPresenter>(), SearchView {

    private val destinationsAdapter = DestinationsAdapter()
    private val selectionListener: DestinationSelectionListener?
        get() = parentFragment as? DestinationSelectionListener
            ?: activity as? DestinationSelectionListener

    private val queryTextWatcher: TextWatcher = object : TextChangeAdapter() {

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            presenter.onQueryChanged(queryEditText.text?.toString())
        }
    }

    override fun createPresenter(): SearchPresenter =
        SearchPresenter(ServiceLocator.getDestinationsInteractor(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        queryInputLayout.setStartIconOnClickListener {
            dismiss()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        queryEditText.addTextChangedListener(queryTextWatcher)
    }

    private fun initRecyclerView() {
        val dividerDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        destinationsRecyclerView.addItemDecoration(dividerDecoration)
        destinationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        destinationsRecyclerView.adapter = destinationsAdapter
        destinationsAdapter.itemClickCallback = { destination ->
            tag
                ?.let { tag ->
                    selectionListener?.onDestinationSelected(destination, tag)
                }
                ?: throw IllegalStateException("SearchDialog requires tag to return destination selection")
            dismiss()
        }
    }

    override fun showList(destinations: List<Destination>) {
        destinationsAdapter.submitList(destinations)
        destinationsRecyclerView.isVisible = destinations.isNotEmpty()
        noMatchesContainer.isVisible = destinations.isEmpty()
    }

    override fun showError(message: String?) {
        message ?: return
        activity?.toast(message)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.hideKeyboard(queryEditText)
    }

    companion object {

        fun newInstance() = SearchDialog()
    }

    interface DestinationSelectionListener {

        fun onDestinationSelected(destination: Destination, tag: String)
    }
}
