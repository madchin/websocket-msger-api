package com.example.controller.feature_member_manage

import com.example.model.MemberDTO
import com.example.service.MemberService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.member(memberService: MemberService) {
    post("/member/add-member") {
        val member = call.receive<MemberDTO>()
        val principal = call.principal<JWTPrincipal>()
        val currentUserId = principal?.payload?.getClaim("uid")?.asString()!!
        val currentMemberWithUpdatedName = member.toMember(currentUserId)

        memberService.addMember(currentMemberWithUpdatedName).also {
            call.respond(HttpStatusCode.Created, it)
        }
    }
    put("/member/update-member-name") {
        val member = call.receive<MemberDTO>()
        val principal = call.principal<JWTPrincipal>()
        val currentUserId = principal?.payload?.getClaim("uid")?.asString()!!
        val currentMemberWithUpdatedName = member.toMember(currentUserId)

        memberService.updateMemberName(currentMemberWithUpdatedName.uid, currentMemberWithUpdatedName.name).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }
    get("/member") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.payload?.getClaim("uid")?.asString()!!

        memberService.getMember(userId).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }
    delete("/member") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.payload?.getClaim("uid")?.asString()!!

        memberService.deleteMember(userId).also {
            call.respond(HttpStatusCode.NoContent)
        }
    }
}