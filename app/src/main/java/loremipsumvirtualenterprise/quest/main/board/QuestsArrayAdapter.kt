package loremipsumvirtualenterprise.quest.main.board

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater.*
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import loremipsumvirtualenterprise.quest.R
import loremipsumvirtualenterprise.quest.model.Quest
import loremipsumvirtualenterprise.quest.model.QuestUser
import loremipsumvirtualenterprise.quest.util.DateHelper
import loremipsumvirtualenterprise.quest.util.FirebaseConstants
import loremipsumvirtualenterprise.quest.util.FirebaseDatabaseUtil

/**
 * Created by root on 2017-11-27.
 */
class QuestsArrayAdapter constructor(context: Context, objects: ArrayList<Quest>?, private val clickListener: (Quest) -> Unit) : RecyclerView.Adapter<QuestsArrayAdapter.Holder>()
{
    //region Attributes

    private var mContext : Context = context
    private var mObjects : ArrayList<Quest>? = objects

    //endregion

    //region Interface

    interface IQuestAdapterListener

    //endregion

    //region Overridden Methods

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val questItem: Quest = mObjects!![position]

        holder?.itemQuestTitle?.text = questItem.title
        holder?.itemQuestDescription?.text = questItem.description
        holder?.itemQuestPublishDate?.text = DateHelper.Companion.formatDate(questItem.publishedAt)
        loadAuthorForHolder(holder, questItem)
        holder?.itemQuestResponses?.text = mContext.resources.getString(R.string.board_item_responses_text)
                .replace("{likes}", if (questItem.likes?.size == null) "0" else questItem.likes?.size.toString())
                .replace("{responses}", if (questItem.responses?.size == null) "0" else questItem.responses?.size.toString())
        holder?.itemQuestRootView?.setOnClickListener { clickListener(questItem)}
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val itemView : View = from(parent?.context).inflate(R.layout.item_board_quest, parent, false)
        return Holder(itemView)
    }

    override fun getItemCount(): Int {
        return if (mObjects != null) {
            mObjects!!.size
        } else {
            0
        }
    }

    //endregion

    // Helpers
    fun loadAuthorForHolder(holder: Holder?, quest: Quest) {

        val publisherDatabaseReference = FirebaseDatabaseUtil.usersNode?.child(quest.publisherUID!!)

        publisherDatabaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    val questUser = QuestUser.createFromDataSnapshot(snapshot)
                    holder?.itemQuestAuthor?.text = "@" + questUser.name
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }

    //region Inner Class

    class Holder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var itemQuestRootView: LinearLayout? = null
        var itemQuestTitle: TextView? = null
        var itemQuestDescription: TextView? = null
        var itemQuestPublishDate: TextView? = null
        var itemQuestAuthor : TextView? = null
        var itemQuestResponses : TextView? = null

        init {
            itemQuestRootView = itemView.findViewById<LinearLayout>(R.id.itemQuestRootView)
            itemQuestTitle = itemView.findViewById<TextView>(R.id.itemQuestTitle)
            itemQuestDescription = itemView.findViewById<TextView>(R.id.itemQuestDescription)
            itemQuestPublishDate = itemView.findViewById<TextView>(R.id.itemQuestPublishDate)
            itemQuestAuthor = itemView.findViewById<TextView>(R.id.itemQuestAuthor)
            itemQuestResponses = itemView.findViewById<TextView>(R.id.itemQuestLikeResponsesCountTextView)
        }
    }

    //endregion
}