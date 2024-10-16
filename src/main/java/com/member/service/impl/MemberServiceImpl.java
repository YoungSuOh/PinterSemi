package com.member.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.board.bean.BoardDTO;
import com.board.dao.BoardDAO;
import com.member.bean.MemberDTO;
import com.member.dao.MemberDAO;
import com.member.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private BoardDAO boardDAO;
	@Autowired
	private HttpSession session;
	
	@Override
	public boolean checkId(String id) {
		MemberDTO memberDTO = memberDAO.isExistId(id);
		if (memberDTO == null) return false;
		else return true;
	}
	
	@Override
	public void join(MemberDTO memberDTO) {
		
		memberDAO.join(memberDTO);
	}

	@Override
	public MemberDTO login(Map<String, String> map) {
		return memberDAO.login(map);
	}

	@Override
	public MemberDTO getMember(String id) {
		return memberDAO.getMember(id);
	}

	@Override
	public void update(MemberDTO memberDTO, MultipartFile userProfileImg) {
		String filePath = session.getServletContext().getRealPath("WEB-INF/storage");
		System.out.println("실제폴더 = " + filePath);
		
		String userOriginalProfile;
		String userProfile;
		File file;
		
		MemberDTO dto = memberDAO.getMember(memberDTO.getId());
		if(userProfileImg != null) {
			userOriginalProfile = userProfileImg.getOriginalFilename();
			userProfile = userOriginalProfile;
			userProfile = UUID.randomUUID().toString();
			file = new File(filePath, userOriginalProfile);
			
			try {
				if (userProfileImg.isEmpty()) {
		        }
				userProfileImg.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			memberDTO.setUserProfile(userProfile);
			memberDTO.setUserOriginalProfile(userOriginalProfile);
		} else { // 업데이트폼에서 이미지를 수정하지 않았을 때
			memberDTO.setUserProfile(dto.getUserProfile());
			memberDTO.setUserOriginalProfile(dto.getUserOriginalProfile());
		}
		memberDAO.update(memberDTO);
	}

	@Override
	public void delete(MemberDTO memberDTO) {
		memberDAO.delete(memberDTO);
	}

	@Override
	public List<BoardDTO> getMypageSup(String seq) {
		if(seq != "") return boardDAO.getMyBoardList(Integer.parseInt(seq));
		else return null;
	}
}
