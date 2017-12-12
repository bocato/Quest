package loremipsumvirtualenterprise.quest.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import loremipsumvirtualenterprise.quest.util.FirebaseConstants
import java.util.*

/**
 * Created by root on 2017-11-27.
 */

class Quest {

    companion object Factory {
        fun create(): Quest = Quest()
        fun createFromDataSnapshot(snapshot: DataSnapshot): Quest {
            val questItem = Quest.create()

            //get current data in a map
            val map = snapshot.value as HashMap<*, *>

            //key will return Firebase ID
            questItem.id = snapshot.key
            questItem.title = map.get("title") as String?
            questItem.description = map.get("description") as String?
            questItem.publishedAt = map.get("publishedAt") as String?
            questItem.publisherUID = map.get("publisherUID") as String?
//                questItem.likes = map.get("likes") as Array<QuestLike>?
//                questItem.responses = map.get("responses") as Array<QuestResponse>?

            return questItem
        }
    }

    var id: String? = null
    var title: String? = null
    var description: String? = null
    var publishedAt: String? = null
    var publisherUID: String? = null
    var likes: ArrayList<QuestLike>? = null
    var responses: ArrayList<QuestResponse>? = null

}