package ar.edu.utn.frc.tup.rodigym.controllers;

import ar.edu.utn.frc.tup.rodigym.dtos.MemberCreateDto;
import ar.edu.utn.frc.tup.rodigym.dtos.MemberResponseDto;
import ar.edu.utn.frc.tup.rodigym.dtos.MemberUpdateDto;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import ar.edu.utn.frc.tup.rodigym.services.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing gym members. Provides endpoints for creating, retrieving, and listing
 * members.
 */
@RestController
@RequestMapping("/members")
public class MemberController {

    private final ModelMapper modelMapper;
    private MemberService memberService;

    /**
     * Constructs a new MemberController.
     *
     * @param memberService the service for member operations.
     * @param modelMapper   the mapper for DTO conversions.
     */
    public MemberController(MemberService memberService, ModelMapper modelMapper) {
        this.memberService = memberService;
        this.modelMapper = modelMapper;
    }

    /**
     * Simple ping endpoint to check if the controller is active.
     *
     * @return A "PONG" response.
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("PONG");
    }

    /**
     * Retrieves a specific member by their unique ID.
     *
     * @param id The unique identifier of the member.
     * @return The member details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long id) {
        Member member = memberService.getMember(id);
        MemberResponseDto memberResponseDto = modelMapper.map(member, MemberResponseDto.class);
        return ResponseEntity.ok(memberResponseDto);
    }

    /**
     * Retrieves a list of all registered members.
     *
     * @return A list of members.
     */
    @GetMapping("")
    public ResponseEntity<List<MemberResponseDto>> getMemberList() {
        List<Member> members = memberService.getMemberList();
        List<MemberResponseDto> memberResponseDtos = members.stream()
                .map(member -> modelMapper.map(member, MemberResponseDto.class))
                .toList();
        return ResponseEntity.ok(memberResponseDtos);
    }

    /**
     * Creates a new member and their associated membership. Validates input using JSR-303
     * annotations.
     *
     * @param memberCreateDto The data for the new member.
     * @return The created member details as a response DTO.
     */
    @PostMapping("")
    public ResponseEntity<MemberResponseDto> createMember(
            @Valid @RequestBody MemberCreateDto memberCreateDto) {
        Member member = memberService.createMember(memberCreateDto);
        MemberResponseDto memberResponseDto = modelMapper.map(member, MemberResponseDto.class);
        return ResponseEntity.ok(memberResponseDto);
    }

    /**
     * Updates an existing member.
     *
     * @param id              the unique identifier of the member to update.
     * @param memberUpdateDto the updated data for the member.
     * @return the updated member details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable Long id,
            @Valid @RequestBody MemberUpdateDto memberUpdateDto) {
        Member member = modelMapper.map(memberUpdateDto, Member.class);
        member.setId(id);
        Member updatedMember = memberService.updateMember(member);
        MemberResponseDto updatedMemberDto = modelMapper.map(updatedMember, MemberResponseDto.class);
        return ResponseEntity.ok(updatedMemberDto);
    }

    /**
     * Deletes a member by their ID.
     *
     * @param id the unique identifier of the member to delete.
     * @return the deleted member details.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MemberResponseDto> deleteMember(@PathVariable Long id) {
        Member deleteMember = memberService.deleteMember(id);
        MemberResponseDto deletedMemberDto = modelMapper.map(deleteMember, MemberResponseDto.class);
        return ResponseEntity.ok(deletedMemberDto);
    }
}
