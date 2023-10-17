package com.example.controller.feature_member_manage

import com.example.domain.dao.service.MemberService
import com.example.domain.model.Member
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*


fun Route.member(memberService: MemberService) {
    post("/member/add-member") {
        val member = call.receive<Member>()
        memberService.createOrUpdateMember(member).also {
            call.respond(HttpStatusCode.Created, it)
        }
    }
    put("/member/update-member-name") {
        val member = call.receive<Member>()
        memberService.updateMemberName(member.uid, member.name).also {
            call.respond(HttpStatusCode.OK)
        }
    }
    get("/member?id={id}") {
        val memberId = call.parameters.getOrFail("id")
        memberService.getMember(memberId).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }
    delete("/member?id={id}") {
        val memberId = call.parameters.getOrFail("id")
        memberService.deleteMember(memberId).also {
            call.respond(HttpStatusCode.NoContent)
        }
    }
}