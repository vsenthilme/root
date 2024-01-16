package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.partnersactivitydeck.PartnersActivityDeck;

@Repository
public interface PartnersActivityDeckRepository extends JpaRepository<PartnersActivityDeck, Long>{

	public List<PartnersActivityDeck> findAll();

	public Optional<PartnersActivityDeck> findByPartnerIdAndDeletionIndicator(String partnersActivityDeckId, long l);

}