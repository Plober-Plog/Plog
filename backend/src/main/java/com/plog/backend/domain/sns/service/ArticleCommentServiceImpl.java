package com.plog.backend.domain.sns.service;

import com.plog.backend.domain.sns.dto.request.ArticleCommentAddRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleCommentDeleteRequestDto;
import com.plog.backend.domain.sns.dto.request.ArticleCommentUpdateRequestDto;
import com.plog.backend.domain.sns.dto.response.ArticleCommentGetResponse;
import com.plog.backend.domain.sns.entity.Article;
import com.plog.backend.domain.sns.entity.ArticleComment;
import com.plog.backend.domain.sns.entity.State;
import com.plog.backend.domain.sns.repository.ArticleCommentRepository;
import com.plog.backend.domain.sns.repository.ArticleCommentRepositorySupport;
import com.plog.backend.domain.sns.repository.ArticleRepository;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.exception.NotAuthorizedRequestException;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("articleCommentService")
public class ArticleCommentServiceImpl implements ArticleCommentService {

    @Value("${notification.url}")
    private String notificationUrl;

    public final ArticleRepository articleRepository;
    public final ArticleCommentRepository articleCommentRepository;
    public final JwtTokenUtil jwtTokenUtil;
    public final UserRepository userRepository;
    private final ArticleCommentRepositorySupport articleCommentRepositorySupport;

    @Transactional
    @Override
    public void addArticleComment(String token, ArticleCommentAddRequestDto articleCommentAddRequestDto) {
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotValidRequestException("addArticleComment - 없는 사용자 입니다.")
                );

        Long articleId = articleCommentAddRequestDto.getArticleId();
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new EntityNotFoundException("일치하는 게시글을 찾을 수 없습니다.")
        );

        ArticleComment articleComment = articleCommentRepository.save(ArticleComment.builder()
                .article(articleRepository.getReferenceById(articleId))
                .user(user)
                .parentId(articleCommentAddRequestDto.getParentId())
                .content(articleCommentAddRequestDto.getContent())
                .state(State.PLAIN.getValue())
                .build());


        // 만약 ParentId가 없다면? 본인이 Parent
        if (articleComment.getParentId() == null || articleComment.getParentId() == 0)
            articleComment.setParentId(articleComment.getArticleCommentId());

        articleCommentRepository.save(articleComment);

        log.info("댓글 등록이 완료되었습니다. 게시글 번호: {}, 회원 번호: {}, 댓글 번호: {}", articleId, userId, articleComment.getArticleCommentId());

        // 게시글 등록자에게 댓글 알림 보내기
        String sourceSearchId = user.getSearchId();
        String targetSearchId = article.getUser().getSearchId();
        if (!sourceSearchId.equals(targetSearchId)) {
            String type = "FRIEND_REQUEST";
            String urlString = String.format("%s/send?sourceSearchId=%s&targetSearchId=%s&type=%s",
                    notificationUrl, sourceSearchId, targetSearchId, type);
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/json");

                int responseCode = conn.getResponseCode();
                log.info("알림 전송 HTTP 응답 코드: " + responseCode);
                conn.disconnect();
            } catch (Exception e) {
                log.error("알림 전송 중 오류 발생", e);
            }
        }
    }

    @Transactional
    @Override
    public void updateArticleComment(String token, ArticleCommentUpdateRequestDto articleCommentUpdateRequestDto) {
        Long commentId = articleCommentUpdateRequestDto.getCommentId();

        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                            return new NotValidRequestException("updateArticleComment - 없는 사용자 입니다.");
                        }
                );

        ArticleComment articleComment = articleCommentRepository.findById(commentId)
                .orElseThrow(() -> {
                    return new NotValidRequestException("updateArticleComment - 없는 댓글 입니다.");
                });

        if (!userId.equals(articleComment.getUser().getUserId()))
            throw new NotAuthorizedRequestException("updateArticleComment - 본인 댓글이 아닙니다. 수정할 수 없습니다.");

        articleComment.setContent(articleCommentUpdateRequestDto.getComment());

        articleCommentRepository.save(articleComment);
        log.info("댓글 수정이 완료되었습니다. 댓글 번호: {}, 회원 번호: {}", commentId, userId);
    }

    @Transactional
    @Override
    public void deleteArticleComment(String token, Long commentId) {

        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                            return new NotValidRequestException("deleteArticleComment - 없는 사용자 입니다.");
                        }
                );

        ArticleComment articleComment = articleCommentRepository.findById(commentId)
                .orElseThrow(() -> {
                    return new NotValidRequestException("deleteArticleComment - 없는 댓글 입니다.");
                });

        if (!userId.equals(articleComment.getUser().getUserId()))
            throw new NotAuthorizedRequestException("deleteArticleComment - 본인 댓글이 아닙니다. 삭제할 수 없습니다.");

        articleComment.setState(State.DELETE);

        articleCommentRepository.save(articleComment);
        log.info("댓글 삭제가 완료되었습니다. 댓글 번호: {}, 회원 번호: {}", commentId, userId);
    }

    @Override
    public List<List<ArticleCommentGetResponse>> getArticleComments(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> {
            return new EntityNotFoundException("getArticleComments - 없는 게시글 입니다.");
        });

        // 부모 댓글들을 먼저 조회
        List<ArticleComment> parentComments = articleCommentRepositorySupport.findParentCommentsByArticleId(article);

        // 댓글 그룹 개수만큼 리스트 할당
        List<List<ArticleCommentGetResponse>> commentsWithReplies = new ArrayList<>(parentComments.size());

        // 각 부모 댓글에 대한 자식 댓글들을 조회하여 리스트에 추가
        for (int i = 0; i < parentComments.size(); i++) {
            commentsWithReplies.add(new ArrayList<>());
            List<ArticleComment> childComments = articleCommentRepositorySupport.findChildCommentsByParentId(parentComments.get(i).getArticleCommentId());
            List<ArticleComment> parentWithChildren = new ArrayList<>();
            parentWithChildren.addAll(childComments); // 해당 댓글 그룹에 댓글들 추가 (index:0 -> root)
            for (ArticleComment articleComment : parentWithChildren) {
                commentsWithReplies.get(i).add(ArticleCommentGetResponse.builder()
                        .articleCommentId(articleComment.getArticleCommentId())
                        .parentId(articleComment.getParentId())
                        .content(articleComment.getContent())
                        .profile(articleComment.getUser().getImage().getImageUrl())
                        .userId(articleComment.getUser().getUserId())
                        .state(articleComment.getState().getValue())
                        .nickname(articleComment.getUser().getNickname())
                        .createDate(LocalDate.from(articleComment.getCreatedAt()))
                        .updateDate(LocalDate.from(articleComment.getUpdatedAt()))
                        .build()
                );
            }
        }
        log.info("댓글 조회이 완료되었습니다. 댓글 그룹 개수: {}", parentComments.size());
        return commentsWithReplies;
    }
}
