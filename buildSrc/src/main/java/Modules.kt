object Modules {
    const val common = ":common"

    object Core {
        const val constants = ":core:constants"
        const val di = ":core:di"
        const val diDagger = ":core:di_dagger"
        const val navigation = ":core:navigation"
        const val ui = ":core:ui"
        const val utils = ":core:utils"
    }

    object TasksFeature {
        const val domain = ":tasks:domain"
        const val data = ":tasks:data"

        object Ui {
            const val library = ":tasks:ui:library"
            const val taskList = ":tasks:ui:task_list"
            const val taskDetails = ":tasks:ui:task_details"
        }
    }
}
