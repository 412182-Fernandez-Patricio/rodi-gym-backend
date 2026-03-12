package ar.edu.utn.frc.tup.rodigym.services.impl;

import ar.edu.utn.frc.tup.rodigym.dtos.MemberCreateDto;
import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import ar.edu.utn.frc.tup.rodigym.entities.MembershipEntity;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import ar.edu.utn.frc.tup.rodigym.repositories.MemberRepository;
import ar.edu.utn.frc.tup.rodigym.services.MemberService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Member getMember(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));
        return modelMapper.map(memberEntity, Member.class);
    }

    @Override
    public List<Member> getMemberList() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        return memberEntities.stream()
                .map(entity -> modelMapper.map(entity, Member.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Member createMember(MemberCreateDto memberCreateDto) {
        MemberEntity memberEntity = modelMapper.map(memberCreateDto, MemberEntity.class);
        
        MembershipEntity membershipEntity = new MembershipEntity();
        membershipEntity.setStartDate(LocalDate.now());
        membershipEntity.setExpirationDate(LocalDate.now().plusMonths(1));
        membershipEntity.setPrice(0.0); // Default price, could be changed later
        
        // Linking both sides for the bidirectional relationship
        membershipEntity.setMember(memberEntity);
        memberEntity.setMembership(membershipEntity);
        
        MemberEntity savedMember = memberRepository.save(memberEntity);
        return modelMapper.map(savedMember, Member.class);
    }

    @Override
    @Transactional
    public Member updateMember(Member member) {
        MemberEntity memberEntity = memberRepository.findById(member.getId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + member.getId()));
        
        modelMapper.map(member, memberEntity);
        MemberEntity updatedMember = memberRepository.save(memberEntity);
        return modelMapper.map(updatedMember, Member.class);
    }

    @Override
    @Transactional
    public void deleteMember(Member member) {
        memberRepository.deleteById(member.getId());
    }
}

