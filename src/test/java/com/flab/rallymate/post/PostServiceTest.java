package com.flab.rallymate.post;

import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.domain.MemberEntity;
import com.flab.rallymate.domain.playground.PlaygroundService;
import com.flab.rallymate.domain.playground.domain.Playground;
import com.flab.rallymate.domain.post.PostService;
import com.flab.rallymate.domain.post.domain.PostEntity;
import com.flab.rallymate.domain.post.domain.PostRepository;
import com.flab.rallymate.domain.post.dto.PostRequestDTO;
import com.flab.rallymate.error.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_MEMBER;
import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_PLAYGROUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostService sut;
    private PostRepository postRepository;
    private MemberService memberService;
    private PlaygroundService playgroundService;


    @BeforeEach
    void setUp() {
        playgroundService = mock(PlaygroundService.class);
        memberService = mock(MemberService.class);
        postRepository = mock(PostRepository.class);
        sut = new PostService(postRepository, memberService, playgroundService);
    }

    @Test
    void addPost_메이트_구인글_등록요청시_등록에_성공한다() throws Exception {
        var postRequestDTO = PostRequestDTO.builder()
                .playgroundId(1L)
                .playTime(120)
                .startTime(LocalDateTime.of(2023, 7, 28, 13, 30))
                .build();
        String email = "sample@sample.com";
        var findMember = MemberEntity.builder().id(2L).name("nathan").build();
        var findPlayground = Playground.builder().id(postRequestDTO.playgroundId()).name("송실내테니스").build();
        var post = PostEntity.from(postRequestDTO);
        post.changeMember(findMember);
        post.changePlayground(findPlayground);

        when(memberService.getCurrentMemberEmail()).thenReturn(email);
        when(memberService.findMemberBy(email)).thenReturn(Optional.ofNullable(findMember));
        when(playgroundService.findPlaygroundBy(postRequestDTO.playgroundId())).thenReturn(Optional.ofNullable(findPlayground));


        sut.addPost(postRequestDTO);


        verify(postRepository).save(post);
        assertEquals(postRequestDTO.playTime(), post.getPlayTime());
        assertEquals(findMember, post.getMember());
        assertEquals(findPlayground, post.getPlayground());
        assertEquals(postRequestDTO.startTime(), post.getStartTime());
    }

    @Test
    void addPost_구인_글_작성_요청시_조회된_회원정보가_없다면_NOT_FOUND_MEMBER_예외가_발생한다() throws Exception {
        var postRequestDTO = PostRequestDTO.builder()
                .playgroundId(1L)
                .playTime(120)
                .startTime(LocalDateTime.of(2023, 7, 28, 13, 30, 0))
                .build();
        String email = "sample@sample.com";
        var findPlayground = Playground.builder().id(postRequestDTO.playgroundId()).name("송실내테니스").build();

        when(memberService.getCurrentMemberEmail()).thenReturn(email);
        when(memberService.findMemberBy(email)).thenReturn(Optional.empty());
        when(playgroundService.findPlaygroundBy(postRequestDTO.playgroundId())).thenReturn(Optional.ofNullable(findPlayground));


        var baseException = assertThrows(BaseException.class, () -> sut.addPost(postRequestDTO));


        assertEquals(baseException.getMessage(), NOT_FOUND_MEMBER.getMessage());
    }

    @Test
    void addPost_구인_글_작성_요청시_조회된_플레이장소가_없다면_NOT_FOUND_PLAYGROUND_예외가_발생한다() throws Exception {
        var postRequestDTO = PostRequestDTO.builder()
                .playgroundId(1L)
                .playTime(120)
                .startTime(LocalDateTime.of(2023, 7, 28, 13, 30, 0))
                .build();
        String email = "sample@sample.com";
        var findMember = MemberEntity.builder().id(2L).name("nathan").build();

        when(memberService.getCurrentMemberEmail()).thenReturn(email);
        when(memberService.findMemberBy(email)).thenReturn(Optional.ofNullable(findMember));
        when(playgroundService.findPlaygroundBy(postRequestDTO.playgroundId())).thenReturn(Optional.empty());


        var baseException = assertThrows(BaseException.class, () -> sut.addPost(postRequestDTO));


        assertEquals(baseException.getMessage(), NOT_FOUND_PLAYGROUND.getMessage());
    }


}