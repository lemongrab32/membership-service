package com.github.lemongrab32.util.mapper;

import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.model.Membership;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MembershipMapper {

	Membership toMembership(MembershipRequest request);

}
