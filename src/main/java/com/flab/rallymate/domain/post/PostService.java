package com.flab.rallymate.domain.post;

import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.playground.PlaygroundService;
import com.flab.rallymate.domain.post.domain.PostEntity;
import com.flab.rallymate.domain.post.domain.PostRepository;
import com.flab.rallymate.domain.post.dto.PostRequestDTO;
import com.flab.rallymate.error.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_MEMBER;
import static com.flab.rallymate.error.ErrorCode.NOT_FOUND_PLAYGROUND;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final PlaygroundService playgroundService;

    public void addPost(PostRequestDTO postRequestDTO) {

        String email = memberService.getCurrentMemberEmail();
        var member = memberService.findMemberBy(email)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
        var playground = playgroundService.findPlaygroundBy(postRequestDTO.playgroundId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PLAYGROUND));

        var post = PostEntity.from(postRequestDTO);
        post.changeMember(member);
        post.changePlayground(playground);

        postRepository.save(post);
    }


}
