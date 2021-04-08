package {{ cookiecutter.package_name }}.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO : Remove this when real entities available
@Entity(tableName = "dummyEntity")
data class DummyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int
)
