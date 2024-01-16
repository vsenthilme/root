package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.message.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

	public List<Message> findAll();
	public Message findByMessageId(Long messageId);
	
	// `LANG_ID`, `CLASS_ID`, `MESSAGE_ID`, `MESSAGE_TYP`, `IS_DELETED`
	Optional<Message> 
		findByLanguageIdAndClassIdAndMessageIdAndMessageTypeAndDeletionIndicator
		(String languageId, Long classId, Long messageId, String messageType, Long deletionIndicator);
}