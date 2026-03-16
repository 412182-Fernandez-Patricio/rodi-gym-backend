package ar.edu.utn.frc.tup.rodigym.controllers;


import ar.edu.utn.frc.tup.rodigym.dtos.MemberCreateDto;
import ar.edu.utn.frc.tup.rodigym.dtos.MemberResponseDto;
import ar.edu.utn.frc.tup.rodigym.dtos.MemberUpdateDto;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import ar.edu.utn.frc.tup.rodigym.services.DummyService;
import ar.edu.utn.frc.tup.rodigym.services.MemberService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing gym members.
 * Provides endpoints for creating, retrieving, and listing members.
 */
@RestController
@RequestMapping("/members")
public class MemberController {

    private final ModelMapper modelMapper;
    private MemberService memberService;

    public MemberController(MemberService memberService, ModelMapper modelMapper) {
        this.memberService = memberService;
        this.modelMapper = modelMapper;
    }

    /**
     * Simple ping endpoint to check if the controller is active.
     * @return A "PONG" response.
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        return ResponseEntity.ok("PONG");
    }

    /**
     * Retrieves a specific member by their unique ID.
     * @param id The unique identifier of the member.
     * @return The member details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long id){
        Member member = memberService.getMember(id);
        MemberResponseDto memberResponseDto = modelMapper.map(member, MemberResponseDto.class);
        return ResponseEntity.ok(memberResponseDto);
    }

    /**
     * Retrieves a list of all registered members.
     * @return A list of members.
     */
    @GetMapping("")
    public ResponseEntity<List<MemberResponseDto>> getMemberList(){
        List<Member> members = memberService.getMemberList();
        List<MemberResponseDto> memberResponseDtos = members.stream()
                .map(member -> modelMapper.map(member, MemberResponseDto.class))
                .toList();
        return ResponseEntity.ok(memberResponseDtos);
    }

    /**
     * Creates a new member and their associated membership.
     * Validates input using JSR-303 annotations.
     * @param memberCreateDto The data for the new member.
     * @return The created member details as a response DTO.
     */
    @PostMapping("")
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberCreateDto memberCreateDto){
        Member member = memberService.createMember(memberCreateDto);
        MemberResponseDto memberResponseDto = modelMapper.map(member, MemberResponseDto.class);
        return ResponseEntity.ok(memberResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable Long id, @Valid @RequestBody MemberUpdateDto memberUpdateDto){
        Member member = modelMapper.map(memberUpdateDto, Member.class);
        member.setId(id);
        Member updatedMember = memberService.updateMember(member);
        MemberResponseDto updatedMemberDto = modelMapper.map(updatedMember, MemberResponseDto.class);
        return ResponseEntity.ok(updatedMemberDto);
    }

}

