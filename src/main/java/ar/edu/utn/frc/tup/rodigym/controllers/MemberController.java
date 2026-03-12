package ar.edu.utn.frc.tup.rodigym.controllers;


import ar.edu.utn.frc.tup.rodigym.dtos.MemberCreateDto;
import ar.edu.utn.frc.tup.rodigym.dtos.MemberResponseDto;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import ar.edu.utn.frc.tup.rodigym.services.DummyService;
import ar.edu.utn.frc.tup.rodigym.services.MemberService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final ModelMapper modelMapper;
    private MemberService memberService;

    public MemberController(MemberService memberService, ModelMapper modelMapper) {
        this.memberService = memberService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        return ResponseEntity.ok("PONG");
    }


    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id){
        return ResponseEntity.ok(memberService.getMember(id));
    }

    @GetMapping("")
    public ResponseEntity<List<Member>> getMemberList(){
        return ResponseEntity.ok(memberService.getMemberList());
    }

    @PostMapping("")
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberCreateDto memberCreateDto){
        Member member = memberService.createMember(memberCreateDto);
        MemberResponseDto memberResponseDto = modelMapper.map(member, MemberResponseDto.class);
        return ResponseEntity.ok(memberResponseDto);
    }
}

