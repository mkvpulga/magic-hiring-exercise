import React from 'react';
import { useGetTodayArrivalsQuery } from './propertiesApi';

interface ArrivalsListProps {
    propertyId: string;
}

export const ArrivalsList: React.FC<ArrivalsListProps> = ({ propertyId }) => {
    // Call the auto-generated hook we created in Step 3
    const { data: arrivals, error, isLoading } = useGetTodayArrivalsQuery(propertyId, {
        skip: !propertyId, // Don't run the query if a property hasn't been chosen yet
    });

    if (!propertyId) return <p>Please select a property to view arrivals.</p>;
    if (isLoading) return <p>Loading today's arrivals...</p>;
    if (error) return <p style={{ color: 'red' }}>Error loading arrivals data.</p>;

    return (
        <div style={{ marginTop: '20px' }}>
            <h3>Today's Arrivals</h3>
            {arrivals?.length === 0 ? (
                <p>No arrivals for this property today.</p>
            ) : (
                <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
                    <thead>
                    <tr style={{ borderBottom: '2px solid #ccc' }}>
                        <th>Guest Name</th>
                        <th>Assigned Unit</th>
                    </tr>
                    </thead>
                    <tbody>
                    {arrivals?.map((arrival) => (
                        <tr key={arrival.id} style={{ borderBottom: '1px solid #eee' }}>
                            <td style={{ padding: '8px 0' }}>{arrival.guestName}</td>
                            <td style={{ padding: '8px 0' }}>
                                {arrival.assignedUnit ? (
                                    <strong>{arrival.assignedUnit.label}</strong>
                                ) : (
                                    <span style={{ color: '#888' }}>Unassigned</span>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};
