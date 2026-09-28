package ar.edu.utn.frc.tup.rodigym.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ar.edu.utn.frc.tup.rodigym.config.MappersConfig;
import ar.edu.utn.frc.tup.rodigym.dtos.MemberCreateDto;
import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import ar.edu.utn.frc.tup.rodigym.enums.MemberStatus;
import ar.edu.utn.frc.tup.rodigym.exceptions.MemberAlreadyExistsException;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import ar.edu.utn.frc.tup.rodigym.repositories.MemberRepository;
import ar.edu.utn.frc.tup.rodigym.services.impl.MemberServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * Spec 001: el alta no regala membresía y un DNI repetido nunca pisa al socio que
 * ya lo tiene.
 */
@DataJpaTest
@Import({MemberServiceImpl.class, MappersConfig.class})
class MemberServiceImplTest {

    /** DNI que no está en el seed. */
    private static final long NEW_DNI = 40123456L;

    /** Socio del seed, activo y con membresía. */
    private static final long SEEDED_DNI = 12345678L;

    /** Socio del seed dado de baja. */
    private static final long INACTIVE_DNI = 33788456L;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldCreateAnActiveMemberWithoutMembership() {
        Member created = memberService.createMember(newMember(NEW_DNI, "Laura"));

        assertThat(created.getStatus()).isTrue();
        assertThat(created.getMembership()).isNull();

        entityManager.flush();
        entityManager.clear();
        MemberEntity stored = memberRepository.findById(NEW_DNI).orElseThrow();
        assertThat(stored.getStatus()).isTrue();
        assertThat(stored.getMembership()).isNull();
    }

    @Test
    void shouldListTheNewMemberAsADebtor() {
        memberService.createMember(newMember(NEW_DNI, "Laura"));
        entityManager.flush();

        assertThat(search(MemberStatus.EXPIRED)).anyMatch(m -> m.getId() == NEW_DNI);
        assertThat(search(MemberStatus.ACTIVE)).noneMatch(m -> m.getId() == NEW_DNI);
    }

    @Test
    void shouldRejectARepeatedIdWithoutOverwritingTheExistingMember() {
        String originalName = memberRepository.findById(SEEDED_DNI).orElseThrow().getName();

        assertThatThrownBy(() -> memberService.createMember(newMember(SEEDED_DNI, "Impostor")))
                .isInstanceOf(MemberAlreadyExistsException.class);

        entityManager.flush();
        entityManager.clear();
        MemberEntity stored = memberRepository.findById(SEEDED_DNI).orElseThrow();
        assertThat(stored.getName()).isEqualTo(originalName);
        assertThat(stored.getMembership()).isNotNull();
    }

    @Test
    void shouldRejectTheIdOfAnInactiveMemberToo() {
        assertThat(memberRepository.findById(INACTIVE_DNI).orElseThrow().getStatus()).isFalse();

        assertThatThrownBy(() -> memberService.createMember(newMember(INACTIVE_DNI, "Martin")))
                .isInstanceOf(MemberAlreadyExistsException.class);
    }

    private List<Member> search(MemberStatus status) {
        return memberService.searchMembers(String.valueOf(NEW_DNI), status, PageRequest.of(0, 20))
                .getContent();
    }

    private static MemberCreateDto newMember(long dni, String name) {
        return new MemberCreateDto(dni, name, "Quiroga", "3515550199");
    }
}
