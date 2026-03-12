package ar.edu.utn.frc.tup.rodigym.services;

import ar.edu.utn.frc.tup.rodigym.dtos.MemberCreateDto;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemberService {

    Member getMember(Long id);

    List<Member> getMemberList();

    Member createMember(MemberCreateDto memberCreateDto);

    Member updateMember(Member member);

    void deleteMember(Member member);

}

