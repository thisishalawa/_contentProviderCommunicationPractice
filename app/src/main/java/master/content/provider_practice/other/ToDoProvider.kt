package master.content.provider_practice.other

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri


class ToDoProvider : ContentProvider() {


    val AUTHORITY = "todolist.example.com"

    val PATH_TODO_LIST = "TODO_LIST"
    val PATH_TODO_PLACE = "TODO_LIST_FROM_PLACE"
    val PATH_TODO_COUNT = "TODOS_COUNT"

    val CONTENT_URI_1: Uri = Uri.parse("content://$AUTHORITY/$PATH_TODO_LIST")
    val CONTENT_URI_2: Uri = Uri.parse("content://$AUTHORITY/$PATH_TODO_PLACE")
    val CONTENT_URI_3: Uri = Uri.parse("content://$AUTHORITY/$PATH_TODO_COUNT")

    val TODOS_LIST = 1
    val TODOS_FROM_SPECIFIC_PLACE = 2
    val TODOS_COUNT = 3

    val MIME_TYPE_1 = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.com.example.todos"
    val MIME_TYPE_2 = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.com.example.todos.place"
    val MIME_TYPE_3 = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.com.example.todocount"

    private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

    /*
    * impl
    *
    *
    *
    * */
    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }


}