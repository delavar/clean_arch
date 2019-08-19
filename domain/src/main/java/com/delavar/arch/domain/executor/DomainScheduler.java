package com.delavar.arch.domain.executor;

import io.reactivex.Scheduler;

public interface DomainScheduler {
  Scheduler io();
  Scheduler ui();
}
