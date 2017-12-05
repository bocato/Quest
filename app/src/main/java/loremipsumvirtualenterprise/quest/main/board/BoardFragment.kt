package loremipsumvirtualenterprise.quest.main.board

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_main_board.*
import kotlinx.android.synthetic.main.toolbar_main.*
import loremipsumvirtualenterprise.quest.R
import loremipsumvirtualenterprise.quest.generic.MainGenericFragment
import loremipsumvirtualenterprise.quest.main.quest.CreateQuestActivity
import loremipsumvirtualenterprise.quest.main.quest.QuestDetailActivity
import loremipsumvirtualenterprise.quest.model.Quest

/**
 * Created by root on 2017-11-26.
 */
class BoardFragment : MainGenericFragment()
{
    //region Attributes

    private var mContext : Context? = null
    private var mQuestsAdapter : QuestsArrayAdapter? = null
    private var mObjects : ArrayList<Quest>? = ArrayList<Quest>()

    //endregion

    //region Companion Object

    companion object {
        fun newInstance(): BoardFragment {
            val fragment = BoardFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    //endregion

    //region Overridden Methods

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = container?.context
        return inflater?.inflate(R.layout.fragment_main_board, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        initViews()
        loadQuests()
    }

    override fun onFragmentReselected() { }

    //endregion

    //region Private Methods

    private fun setUpToolbar() {
        mainToolbarTitleTextView.text = resources.getString(R.string.main_board_bottombar_text)
        mainToolbarActionButton.visibility = View.VISIBLE
        mainToolbarActionButton.setOnClickListener {
            startActivity(SearchActivity.getActivityIntent(context))
        }
    }

    private fun initViews() {
        mainBoardRecyclerView.itemAnimator = DefaultItemAnimator()
        mainBoardRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
        mQuestsAdapter =  QuestsArrayAdapter(context, mObjects) { item: Quest ->
            startActivity(QuestDetailActivity.getActivityIntent(context, item.questId!!))
        }
        mainBoardRecyclerView.adapter = mQuestsAdapter

        boardFloatingActionButton.setOnClickListener {
            startActivity(CreateQuestActivity.getActivityIntent(context))
        }
    }

    private fun loadQuests() {
        mObjects?.add(Quest(1))
        mObjects?.add(Quest(2))
        mObjects?.add(Quest(3))
        mObjects?.add(Quest(4))
        mQuestsAdapter?.notifyDataSetChanged()
    }

    //endregion
}
