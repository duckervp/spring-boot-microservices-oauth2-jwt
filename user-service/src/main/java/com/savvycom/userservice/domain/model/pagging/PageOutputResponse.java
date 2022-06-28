package com.savvycom.userservice.domain.model.pagging;

import com.savvycom.userservice.domain.message.ExtendedMessage;
import com.savvycom.userservice.domain.model.getUser.UserOutput;

/**
 * Define generic response return type for spring docs api
 */
public class PageOutputResponse extends ExtendedMessage<PageOutput<UserOutput>> {
}
