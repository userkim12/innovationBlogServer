package com.innovation.demo.mapper;


import com.innovation.demo.dto.BoardPatchDto;
import com.innovation.demo.dto.BoardPostDto;
import com.innovation.demo.dto.BoardResponseDto;
import com.innovation.demo.entity.Board;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoardMapper {
//    @Mapping(target = "boardId", ignore = true)
    Board BoardPostDtoToBoard(BoardPostDto post);
//    @Mapping(target = "boardId", ignore = true)
    Board BoardPatchDtoToBoard(BoardPatchDto patch);
    BoardResponseDto BoardToBoardResponseDto(Board board);
    List<BoardResponseDto> BoardsToBoardResponseDtos(List<Board> boards);
}
