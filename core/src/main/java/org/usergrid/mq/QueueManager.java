/*******************************************************************************
 * Copyright 2012 Apigee Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.usergrid.mq;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


import org.usergrid.persistence.CounterResolution;
import org.usergrid.persistence.Results;
import org.usergrid.persistence.exceptions.TransactionNotFoundException;


public interface QueueManager {

	public Queue getQueue(String queuePath);

	public Queue updateQueue(String queuePath, Map<String, Object> properties);

	public Queue updateQueue(String queuePath, Queue queue);

	public Message postToQueue(String queuePath, Message message);

	public List<Message> postToQueue(String queuePath, List<Message> messages);

	public QueueResults getFromQueue(String queuePath, QueueQuery query);

	public Message getMessage(UUID messageId);

	public UUID getNewConsumerId();

	public QueueSet getQueues(String firstQueuePath, int limit);

	public QueueSet subscribeToQueue(String publisherQueuePath,
			String subscriberQueuePath);

	public QueueSet unsubscribeFromQueue(String publisherQueuePath,
			String subscriberQueuePath);

	public QueueSet addSubscribersToQueue(String publisherQueuePath,
			List<String> subscriberQueuePaths);

	public QueueSet removeSubscribersFromQueue(String publisherQueuePath,
			List<String> subscriberQueuePaths);

	public QueueSet subscribeToQueues(String subscriberQueuePath,
			List<String> publisherQueuePaths);

	public QueueSet unsubscribeFromQueues(String subscriberQueuePath,
			List<String> publisherQueuePaths);

	public QueueSet getSubscribers(String publisherQueuePath,
			String firstSubscriberQueuePath, int limit);

	public QueueSet getSubscriptions(String subscriberQueuePath,
			String firstSubscriptionQueuePath, int limit);

	public QueueSet searchSubscribers(String publisherQueuePath, Query query);

	public QueueSet getChildQueues(String publisherQueuePath,
			String firstQueuePath, int count);

	public abstract void incrementAggregateQueueCounters(String queuePath,
			String category, String counterName, long value);

	public abstract Results getAggregateQueueCounters(String queuePath,
			String category, String counterName, CounterResolution resolution,
			long start, long finish, boolean pad);

	public abstract Results getAggregateQueueCounters(String queuePath,
			CounterQuery query) throws Exception;

	public abstract Set<String> getQueueCounterNames(String queuePath)
			throws Exception;

	public abstract void incrementQueueCounters(String queuePath,
			Map<String, Long> counts);

	public abstract void incrementQueueCounter(String queuePath, String name,
			long value);

	public abstract Map<String, Long> getQueueCounters(String queuePath)
			throws Exception;

	/**
	 * Renew a transaction.  Will remove the current transaction and return a new one
	 * @param queuePath The path to the queue
	 * @param transactionId The transaction id
	 * @param query
	 * @return
	 * @throws TransactionNotFoundException 
	 */
	public abstract UUID renewTransaction(String queuePath,UUID transactionId,QueueQuery query) throws TransactionNotFoundException;
	
	/**
	 * Deletes the transaction for the consumer
	 * @param queuePath The path to the queue
	 * @param transactionId The transaction id
	 * @param query
	 */
	public abstract void deleteTransaction(String queuePath,UUID transactionId, QueueQuery query);
}
