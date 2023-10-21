import com.example.config.*
import com.example.dao.DatabaseFactory
import com.example.dao.RepositoryFactory
import com.example.service.ServiceFactory
import com.example.socket.SocketFactory
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import java.io.File

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
    DatabaseFactory.init(true, environment = environment)
    RepositoryFactory.init()
    ServiceFactory.init(
        RepositoryFactory.chatRepository,
        RepositoryFactory.messageRepository,
        RepositoryFactory.memberRepository,
        RepositoryFactory.userRepository
    )
    SocketFactory.init(ServiceFactory.chatService, ServiceFactory.memberService)
    configureHTTP()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureSockets()
    configureRouting(
        ServiceFactory.chatService,
        ServiceFactory.authService,
        SocketFactory.chatMemberHandler,
        SocketFactory.chatRoomHandler
    )
}