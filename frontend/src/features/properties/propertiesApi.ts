import { gql } from "graphql-request";
import { api } from "../../api/api";

/**
 * WORKED EXAMPLE of an RTK Query GraphQL endpoint.
 * Copy this shape for your own query.
 */

export type Property = {
  id: string;
  code: string;
  name: string;
  timezone: string;
};

const PropertiesDocument = gql`
  query Properties {
    properties {
      id
      code
      name
      timezone
    }
  }
`;

export type Unit = {
  id: string;
  label: string;
}

export type Reservation = {
  id: string;
  guestName: string;
  arrival: string;
  assignedUnit: Unit | null; // Nullable because units are optional
}

const TodayArrivalsDocument = `
  query GetTodayArrivals($propertyId: ID!) {
    todayArrivals(propertyId: $propertyId) {
      id
      guestName
      arrival
      assignedUnit {
        id
        label
      }
    }
  }
`;

export const propertiesApi = api.injectEndpoints({
  endpoints: (build) => ({
    getProperties: build.query<Property[], void>({
      query: () => ({ document: PropertiesDocument }),
      transformResponse: (res: { properties: Property[] }) => res.properties,
      providesTags: ["Property"],
    }),

    // Add this new endpoint right here:
    getTodayArrivals: build.query<Reservation[], string>({
      query: (propertyId) => ({
        document: TodayArrivalsDocument,
        variables: { propertyId }
      }),
      transformResponse: (res: { todayArrivals: Reservation[] }) => res.todayArrivals,
      providesTags: ["Arrival"],
    }),
  }),
});

// Export the auto-generated hook at the bottom of the file:
export const { useGetPropertiesQuery, useGetTodayArrivalsQuery } = propertiesApi;
