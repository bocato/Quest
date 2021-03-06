package loremipsumvirtualenterprise.quest.main.board

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_quest_detail.*
import kotlinx.android.synthetic.main.fragment_main_board.*
import kotlinx.android.synthetic.main.layout_empty_list.*
import kotlinx.android.synthetic.main.toolbar_main.*
import loremipsumvirtualenterprise.quest.R
import loremipsumvirtualenterprise.quest.generic.MainGenericFragment
import loremipsumvirtualenterprise.quest.generic.QuestGenericProgress
import loremipsumvirtualenterprise.quest.main.quest.CreateQuestActivity
import loremipsumvirtualenterprise.quest.main.quest.QuestDetailActivity
import loremipsumvirtualenterprise.quest.model.Quest
import loremipsumvirtualenterprise.quest.model.QuestLike
import loremipsumvirtualenterprise.quest.model.QuestResponse
import loremipsumvirtualenterprise.quest.util.FirebaseDatabaseUtil
import loremipsumvirtualenterprise.quest.util.NetworkHelper

/**
 * Created by root on 2017-11-26.
 */
class BoardFragment : MainGenericFragment()
{
    // Constants
    private val TAG = "BoardFragment"

    // Properties
    private var mContext : Context? = null
    private var mQuestsAdapter : QuestsArrayAdapter? = null
    private var mQuestsList : ArrayList<Quest> = ArrayList<Quest>()

    // Instantiation
    companion object {
        fun newInstance(): BoardFragment {
            val fragment = BoardFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    // Listeners
    private fun configureListeners() {
        if (FirebaseDatabaseUtil.questsNode != null) {
            FirebaseDatabaseUtil.questsNode.orderByKey().addValueEventListener(questItemsListener)
        }
    }

    var questItemsListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            loadDataFromSnapshot(dataSnapshot)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            mProgress?.hide()
            Log.w(TAG, "loadItem:onCancelled", databaseError.toException())
            emptyLayout?.visibility = View.VISIBLE
            mainBoardRecyclerView?.visibility = View.GONE
        }
    }

    private fun loadDataFromSnapshot(dataSnapshot: DataSnapshot) {

        val items = dataSnapshot.children.iterator()
        mQuestsList.clear()

        if (emptyLayout?.visibility == View.VISIBLE) {
            emptyLayout?.visibility = View.GONE
            mainBoardRecyclerView.visibility = View.VISIBLE
        }

        //Check if current database contains any collection
        if (items.hasNext()) {

            val itemsIterator = items.iterator()

            //check if the collection has any to do items or not
            while (itemsIterator.hasNext()) {

                //get current item
                val currentItem = itemsIterator.next()
                val questItem = Quest.createFromDataSnapshot(currentItem)

                // add to list
               if (questItem != null){
                   mQuestsList.add(questItem)
               }

            }
        }

        //alert adapter that has changed
        activity.runOnUiThread {
            if (mQuestsList.size == 0) {
                emptyListGreetingTextView.text = resources.getString(R.string.board_no_items_greeting_text)
                emptyListTextTextView.text = resources.getString(R.string.board_no_items_text)
                emptyListSuggestionTextView.text = resources.getString(R.string.board_no_items_suggestion_text)
                noInternetReconnectButton.visibility = View.GONE
                emptyLayout.visibility = View.VISIBLE
                mainBoardRecyclerView.visibility = View.GONE
            } else {
                mProgress?.hide()
                mQuestsAdapter?.notifyDataSetChanged()
                mainBoardRecyclerView.visibility = View.VISIBLE
                noInternetReconnectButton.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
                boardFloatingActionButton.visibility = View.VISIBLE
            }
        }
    }

    // Lifecycle
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = container?.context
        return inflater?.inflate(R.layout.fragment_main_board, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        configureListeners()
        setUpToolbar()
    }

    override fun onFragmentReselected() { }

    // Configuration
    private fun setUpToolbar() {
        mainToolbarTitleTextView.text = resources.getString(R.string.main_board_bottombar_text)
        mainToolbarActionButton.visibility = View.VISIBLE
        mainToolbarActionButton.setOnClickListener {
            startActivity(SearchActivity.getActivityIntent(context))
        }
    }

    private fun initViews() {
        mProgress = QuestGenericProgress(context)
        mainBoardRecyclerView.itemAnimator = DefaultItemAnimator()
        mainBoardRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
        mQuestsAdapter =  QuestsArrayAdapter(context, mQuestsList) { item: Quest ->
            startActivity(QuestDetailActivity.getActivityIntent(context, item.id!!))
        }
        mainBoardRecyclerView.adapter = mQuestsAdapter

        boardFloatingActionButton.setOnClickListener { startActivity(CreateQuestActivity.getActivityIntent(context)) }
//        noInternetReconnectButton.setOnClickListener {
//            configureListeners()
//        }
    }

}
