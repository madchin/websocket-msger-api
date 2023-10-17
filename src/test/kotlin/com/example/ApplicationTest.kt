import com.example.domain.dao.Services
import com.example.domain.dao.service.*
import com.example.domain.model.Chat
import com.example.domain.model.Member
import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.plugins.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import java.io.File

class FakeServices : Services {
    override val chatService: ChatService = object : ChatService {
        override suspend fun createChat(chat: Chat): Chat = Chat("10", "xd")

        override suspend fun getChat(id: String): Chat = Chat("10", "xd")

        override suspend fun changeChatName(id: String, name: String): Boolean = true

        override suspend fun deleteChat(id: String): Boolean = true

        override suspend fun joinChat(chatId: String, memberUid: String): Chat = Chat("10", "xd")
    }
    override val memberService: MemberService = object : MemberService {
        override suspend fun createOrUpdateMember(member: Member): Member = Member(uid = "xD", name = "xD")
        override suspend fun getMember(uid: String): Member = Member(uid = "xD", name = "xD")

        override suspend fun updateMemberName(uid: String, name: String): Boolean = true

        override suspend fun deleteMember(uid: String): Boolean = true
    }
    override val messageService: MessageService = object : MessageService {
        override suspend fun saveMessage(message: Message): Boolean = true

        override suspend fun readMessages(chatId: String): List<Message> = emptyList()
    }
    override val userService: UserService = object : UserService {
        override suspend fun getUser(user: User): User =
            User(id = "xD", username = "xxxx", email = "aaaa", password = "aaaa")

        override suspend fun createUser(user: User): Boolean = true

        override suspend fun updateUserUsername(username: String): Boolean = true

        override suspend fun updateUserPassword(user: User): Boolean = true

        override suspend fun deleteUser(username: String): Boolean = true
    }
    override val memberChatService: MemberChatService = object : MemberChatService {
        override suspend fun getMemberAndChat(chatId: String, memberId: String): Pair<Member, Chat> =
            Pair(Member("xd", "xd"), Chat("xd", "xd"))
    }
}

fun main(args: Array<String>) {
    val keyStoreFile = File("./keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "foobar")
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //val chatRoomSocketHandler = ChatRoomSocketHandlerImpl()
    //val chatMemberSocketHandler = ChatMemberSocketHandlerImpl(services.chatService,services.memberChatService, services.messageService)
    configureHTTP()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    //configureSockets(chatRoomSocketHandler,chatMemberSocketHandler)
    configureRouting(FakeServices())
}