package com.innovation.demo.service;

import com.innovation.demo.dto.CommentRequestDto;
import com.innovation.demo.dto.CommentResponseDto;
import com.innovation.demo.entity.Board;
import com.innovation.demo.entity.Comment;
import com.innovation.demo.entity.User;
import com.innovation.demo.repository.BoardRepository;
import com.innovation.demo.repository.CommentRepository;
import com.innovation.demo.repository.UserRepository;
import com.innovation.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Comment createComment(CommentRequestDto commentRequestDto, String token) {
        System.out.println("Token value in createComment: " + token);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username).orElse(null);
        System.out.println(commentRequestDto.getBoardId());
        Board board = boardRepository.findById(commentRequestDto.getBoardId()).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );

        Comment comment = new Comment(commentRequestDto.getContent(), board, user);

        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, CommentRequestDto commentRequestDto, String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username).orElse(null);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RuntimeException("일치하는 댓글이 없습니다.")
        );
        if(user.getRole() == User.Role.ADMIN || username.equals(comment.getUser().getUsername())) {
            comment.setContent(commentRequestDto.getContent());
        }


        return comment;
    }

    public void deleteComment(Long commentId, String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username).orElse(null);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 댓글입니다.")
        );

        if (user.getRole() == User.Role.ADMIN || username.equals(comment.getUser().getUsername())) {
            commentRepository.delete(comment);
        }
    }

    public CommentResponseDto entityToResponseDto(Comment comment) {
        return new CommentResponseDto(comment);
    }

//    public List<CommentResponseDto> entityListToResponseDtoList(List<Comment> commentList) {
//
//        return commentList.stream().map(CommentResponseDto::new).toList();
//    }
}
