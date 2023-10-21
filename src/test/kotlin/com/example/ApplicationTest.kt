import com.example.config.*
import com.example.dao.DatabaseFactory
import com.example.dao.RepositoryFactory
import com.example.dao.RepositoryTestFactory
import com.example.service.ServiceFactory
import com.example.service.ServiceTestFactory
import com.example.socket.SocketFactory
import com.example.socket.SocketTestFactory
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
    RepositoryTestFactory.init()
    ServiceTestFactory.init(
        RepositoryTestFactory.chatRepository,
        RepositoryTestFactory.messageRepository,
        RepositoryTestFactory.memberRepository,
        RepositoryTestFactory.userRepository
    )
    SocketTestFactory.init(ServiceTestFactory.chatService, ServiceTestFactory.memberService)
    configureHTTP()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureSockets()
    configureRouting(
        ServiceTestFactory.chatService,
        ServiceTestFactory.authService,
        SocketTestFactory.chatMemberHandler,
        SocketTestFactory.chatRoomHandler
    )
}