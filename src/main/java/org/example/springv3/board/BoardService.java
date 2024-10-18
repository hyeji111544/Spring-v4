package org.example.springv3.board;

import lombok.RequiredArgsConstructor;
import org.example.springv3.core.error.ex.ExceptionApi403;
import org.example.springv3.core.error.ex.ExceptionApi404;
import org.example.springv3.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;

    public BoardResponse.PageDTO 게시글목록보기(String title, int page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "id");
        if (title == null) {
            Page<Board> boardPG = boardRepository.findAll(pageable);
            return new BoardResponse.PageDTO(boardPG, "");
        } else {
            Page<Board> boardPG = boardRepository.mFindAll(title, pageable);
            return new BoardResponse.PageDTO(boardPG, title);
        }
    }


    @Transactional
    public void 게시글삭제하기(Integer id, User sessionUser) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("게시글을 찾을 수 없습니다"));

        if (board.getUser().getId() != sessionUser.getId()) {
            throw new ExceptionApi403("작성자가 아닙니다.");
        }

        boardRepository.deleteById(id);
    }


    @Transactional
    public BoardResponse.DTO 게시글쓰기(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        Board boardEntity = saveDTO.toEntity(sessionUser);
        Board boardPS = boardRepository.save(boardEntity);
        return new BoardResponse.DTO(boardPS);
    }

    public BoardResponse.DTO 게시글수정화면(int id, User sessionUser) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("게시글을 찾을 수 없습니다"));

        if (board.getUser().getId() != sessionUser.getId()) {
            throw new ExceptionApi403("게시글 수정 권한이 없습니다.");
        }
        return new BoardResponse.DTO(board);
    }

    @Transactional
    public BoardResponse.DTO 게시글수정(int id, BoardRequest.UpdateDTO updateDTO, User sessionUser) {
        // 1. 게시글 조회 (없으면 404)
        Board boardPS = boardRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("게시글을 찾을 수 없습니다"));

        // 2. 권한체크
        if (boardPS.getUser().getId() != sessionUser.getId()) {
            throw new ExceptionApi403("게시글을 수정할 권한이 없습니다");
        }
        // 3. 게시글 수정
        boardPS.setTitle(updateDTO.getTitle());
        boardPS.setContent(updateDTO.getContent());
        return new BoardResponse.DTO(boardPS);
    }

    public BoardResponse.DetailDTO 게시글상세보기(User sessionUser, Integer boardId) {
        Board boardPS = boardRepository.mFindByIdWithReply(boardId)
                .orElseThrow(() -> new ExceptionApi404("게시글이 없습니다."));
        return new BoardResponse.DetailDTO(boardPS, sessionUser);
    }
}
